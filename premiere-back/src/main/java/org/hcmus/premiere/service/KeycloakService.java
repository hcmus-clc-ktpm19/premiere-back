package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.PasswordDto;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {

  UserRepresentation getCurrentUser();

  RoleRepresentation getCurrentUserRole();

  void changePassword(PasswordDto passwordDto);
}
