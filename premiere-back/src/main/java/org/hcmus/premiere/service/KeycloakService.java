package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.PasswordDto;
import org.hcmus.premiere.model.dto.FullInfoUserDto;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.prepost.PreAuthorize;

public interface KeycloakService {

  UserRepresentation getCurrentUser();

  RoleRepresentation getCurrentUserRole();

  Long getUserIdByUsername(String username);

  @PreAuthorize("@SecurityUtils.employeeOrAdmin")
  void createCustomer(FullInfoUserDto fullInfoUserDto);

  @PreAuthorize("hasRole('PREMIERE_ADMIN')")
  Long saveEmployee(FullInfoUserDto fullInfoUserDto);

  void changePassword(PasswordDto passwordDto);

  boolean isPasswordCorrect(String username, String password);

  void resetPassword(PasswordDto passwordDto);
}
