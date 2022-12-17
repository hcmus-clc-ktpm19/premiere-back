package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.PasswordDto;
import javax.ws.rs.core.Response;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {

  UserRepresentation getCurrentUser();

  RoleRepresentation getCurrentUserRole();

  void createUser(RegisterAccountDto registerAccountDto);

  void changePassword(PasswordDto passwordDto);

  Boolean isPasswordCorrect(String username, String password);
  void resetPassword(PasswordDto passwordDto);
}
