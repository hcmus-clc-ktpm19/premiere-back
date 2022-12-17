package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.WrongPasswordException.WRONG_PASSWORD_MESSAGE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.PasswordDto;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.model.exception.WrongPasswordException;
import org.hcmus.premiere.service.KeycloakService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.json.JSONObject;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

  private final RealmResource realmResource;
  private final RestTemplate restTemplate;

  @Override
  public UserRepresentation getCurrentUser() {
    KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    return realmResource
        .users()
        .get(principal.getName())
        .toRepresentation();
  }

  @Override
  public RoleRepresentation getCurrentUserRole() {
    KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Set<String> allRoleNames = EnumSet.allOf(PremiereRole.class).stream().map(Enum::name)
        .collect(Collectors.toSet());

    return realmResource
        .users()
        .get(principal.getName())
        .roles()
        .realmLevel()
        .listAll()
        .stream()
        .filter(roleRepresentation -> allRoleNames.contains(roleRepresentation.getName()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Boolean isPasswordCorrect(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

    map.add("grant_type", "password");
    map.add("client_id", "premiere-client");
    map.add("username", username);
    map.add("password", password);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(
          "http://localhost:8180/realms/premiere-realm/protocol/openid-connect/token", request,
          String.class);
      return response.getStatusCode() == HttpStatus.OK;
    } catch (Exception e) {
      throw new WrongPasswordException(WRONG_PASSWORD_MESSAGE, password);
    }
  }

  @Override
  public void changePassword(PasswordDto passwordDto) {
    KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    if (isPasswordCorrect(passwordDto.getUsername(), passwordDto.getCurrentPassword())) {
      CredentialRepresentation credential = new CredentialRepresentation();
      credential.setType(CredentialRepresentation.PASSWORD);
      credential.setValue(passwordDto.getNewPassword());
      credential.setTemporary(false);
      realmResource
          .users()
          .get(principal.getName())
          .resetPassword(credential);
    } else {
      throw new WrongPasswordException(WRONG_PASSWORD_MESSAGE, passwordDto.getUsername());
    }
  }

  @Override
  public void resetPassword(PasswordDto passwordDto) {
    // search user by email in realm resource
    UserRepresentation userRepresentation = realmResource.users()
        .search(null, null, null, passwordDto.getEmail(), null, null, null, null)
        .stream()
        .findFirst()
        .get();
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(passwordDto.getNewPassword());
    credential.setTemporary(false);
    userRepresentation.setCredentials(Collections.singletonList(credential));

    // update user to keycloak
    realmResource.users().get(userRepresentation.getId()).update(userRepresentation);
  }
}
