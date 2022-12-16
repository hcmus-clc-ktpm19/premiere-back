package org.hcmus.premiere.service.impl;

import java.net.URI;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.service.KeycloakService;
import org.hcmus.premiere.service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

  private final RealmResource realmResource;

  private final UserService userService;

  @Override
  public UserRepresentation getCurrentUser() {
    KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return realmResource
        .users()
        .get(principal.getName())
        .toRepresentation();
  }

  @Override
  public RoleRepresentation getCurrentUserRole() {
    KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Set<String> allRoleNames = EnumSet.allOf(PremiereRole.class).stream().map(Enum::name).collect(Collectors.toSet());

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
  public void createUser(RegisterAccountDto registerAccountDto) {
    User user = userService.saveUser(registerAccountDto);

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

}
