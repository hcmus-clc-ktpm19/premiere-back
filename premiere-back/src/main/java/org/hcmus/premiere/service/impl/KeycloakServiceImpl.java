package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.WrongPasswordException.WRONG_PASSWORD_MESSAGE;

import java.net.URI;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.common.Constants;
import org.hcmus.premiere.model.dto.PasswordDto;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.model.exception.WrongPasswordException;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.KeycloakService;
import org.hcmus.premiere.service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

  private final RealmResource realmResource;

  private final RestTemplate restTemplate;

  private final UserService userService;

  private final CreditCardService creditCardService;

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
  public void createCustomer(RegisterAccountDto registerAccountDto) {
    if(!registerAccountDto.getRole().equals(PremiereRole.CUSTOMER.name())) {
      throw new IllegalArgumentException("Account's role must be CUSTOMER");
    }
    createUser(registerAccountDto);
  }

  @Override
  public void createEmployee(RegisterAccountDto registerAccountDto) {
    if(!registerAccountDto.getRole().equals(PremiereRole.EMPLOYEE.name())) {
      throw new IllegalArgumentException("Account's role must be EMPLOYEE");
    }
    createUser(registerAccountDto);
  }

  private void createUser(RegisterAccountDto registerAccountDto) {
    User user = userService.saveUser(registerAccountDto);
    if(registerAccountDto.getRole().equals(PremiereRole.CUSTOMER.name())) {
      creditCardService.saveCreditCard(user);
    }

    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(registerAccountDto.getUsername());
    userRepresentation.setFirstName(user.getFirstName());
    userRepresentation.setLastName(user.getLastName());
    userRepresentation.setEmail(user.getEmail());
    userRepresentation.setEnabled(true);
    userRepresentation.singleAttribute("userId", String.valueOf(user.getId()));

    Response response = realmResource.users().create(userRepresentation);
    String userId = getCreatedId(response);
    UserResource userResource = realmResource.users().get(userId);

    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setTemporary(false);
    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
    credentialRepresentation.setValue(registerAccountDto.getPassword());
    userResource.resetPassword(credentialRepresentation);

    RoleRepresentation roleRepresentation = realmResource.roles().get(registerAccountDto.getRole()).toRepresentation();
    userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
  }

  private String getCreatedId(Response response) {
    URI location = response.getLocation();
    if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
      Response.StatusType statusInfo = response.getStatusInfo();
      throw new WebApplicationException("Create method returned status " +
          statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode() + "); expected status: Created (201)", response);
    }
    if (location == null) {
      return null;
    }
    String path = location.getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }


  @Override
  public Boolean isPasswordCorrect(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

    map.add("grant_type", "password");
    map.add("client_id", "premiere-client");
    map.add("client_secret", "HvdzEq0XOcO2DQJnQEyXpCToUojGlfEj");
    map.add("username", username);
    map.add("password", password);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(
          Constants.KEYCLOAK_TOKEN_URL, request,
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
