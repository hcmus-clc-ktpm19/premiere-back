package org.hcmus.premiere.service;

import java.util.Set;
import org.hcmus.premiere.model.dto.EmployeeStatusDto;
import org.hcmus.premiere.model.dto.FullInfoUserDto;
import org.hcmus.premiere.model.dto.PasswordDto;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.prepost.PreAuthorize;

public interface KeycloakService {

  UserRepresentation getCurrentUser();

  RoleRepresentation getCurrentUserRole();

  Long getUserIdByUsername(String username);

  @PreAuthorize("@SecurityUtils.employeeOrAdmin")
  void createCustomer(FullInfoUserDto fullInfoUserDto);

  @PreAuthorize("hasRole('EMPLOYEE')")
  Long saveCustomer(FullInfoUserDto fullInfoUserDto);

  @PreAuthorize("hasRole('EMPLOYEE')")
  Set<UserRepresentation> getAllCustomers();

  @PreAuthorize("hasRole('EMPLOYEE')")
  UserRepresentation getCustomerById(Long id);

  @PreAuthorize("hasRole('PREMIERE_ADMIN')")
  Long saveEmployee(FullInfoUserDto fullInfoUserDto);

  @PreAuthorize("hasRole('PREMIERE_ADMIN')")
  Set<UserRepresentation> getAllEmployees();

  @PreAuthorize("hasRole('PREMIERE_ADMIN')")
  UserRepresentation getEmployeeById(Long id);

  @PreAuthorize("hasRole('PREMIERE_ADMIN')")
  void changeEmployeeAccountStatus(EmployeeStatusDto employeeStatusDto);

  void changePassword(PasswordDto passwordDto);

  boolean isPasswordCorrect(String username, String password);

  void resetPassword(PasswordDto passwordDto);
}
