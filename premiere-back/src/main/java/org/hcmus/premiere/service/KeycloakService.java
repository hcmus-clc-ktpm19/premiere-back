package org.hcmus.premiere.service;

import javax.ws.rs.core.Response;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {

  UserRepresentation getCurrentUser();

  RoleRepresentation getCurrentUserRole();

  void createUser(RegisterAccountDto registerAccountDto);
}
