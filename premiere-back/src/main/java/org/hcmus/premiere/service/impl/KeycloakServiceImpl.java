package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.enums.PremiereRole.CUSTOMER;
import static org.hcmus.premiere.model.enums.PremiereRole.EMPLOYEE;
import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND_MESSAGE;
import static org.hcmus.premiere.model.exception.WrongPasswordException.WRONG_PASSWORD_I18N_PLACEHOLDER;
import static org.hcmus.premiere.model.exception.WrongPasswordException.WRONG_PASSWORD_MESSAGE;

import java.net.URI;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.EmployeeStatusDto;
import org.hcmus.premiere.model.dto.FullInfoUserDto;
import org.hcmus.premiere.model.dto.PasswordDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.model.exception.IllegalRoleAssignException;
import org.hcmus.premiere.model.exception.UserNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

  private final RealmResource realmResource;

  private final RestTemplate restTemplate;

  private final UserService userService;

  private CreditCardService creditCardService;

  @Autowired
  public void setCreditCardServiceImpl(CreditCardService creditCardService) {
    this.creditCardService = creditCardService;
  }

  public CreditCardService getCreditCardService() {
    return creditCardService;
  }

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
  public Long getUserIdByUsername(String username) {
    return Long.valueOf(realmResource
        .users()
        .search(username)
        .stream()
        .findFirst()
        .orElseThrow(
            () -> new UserNotFoundException("User not found", username, "AUTH.USER.NOT_FOUND"))
        .getAttributes().get("userId").get(0));
  }

  @Override
  public void createCustomer(FullInfoUserDto fullInfoUserDto) {
    if (!CUSTOMER.value.equals(fullInfoUserDto.getRole())) {
      throw new IllegalRoleAssignException(
          "Account's role must be CUSTOMER",
          IllegalRoleAssignException.ASSIGN_ILLEGAL_CUSTOMER_ROLE
      );
    }
    createUser(fullInfoUserDto);
  }

  @Override
  public Long saveCustomer(FullInfoUserDto fullInfoUserDto) {
    if (!CUSTOMER.value.equals(fullInfoUserDto.getRole())) {
      throw new IllegalRoleAssignException(
          "Account's role must be CUSTOMER",
          IllegalRoleAssignException.ASSIGN_ILLEGAL_CUSTOMER_ROLE
      );
    }
    return fullInfoUserDto.getId() == null ? createUser(fullInfoUserDto) : updateUser(fullInfoUserDto);
  }

  @Override
  public Long saveEmployee(FullInfoUserDto fullInfoUserDto) {
    if (!EMPLOYEE.value.equals(fullInfoUserDto.getRole())) {
      throw new IllegalRoleAssignException(
          "Account's role must be EMPLOYEE",
          IllegalRoleAssignException.ASSIGN_ILLEGAL_EMPLOYEE_ROLE
      );
    }
    return fullInfoUserDto.getId() == null ? createUser(fullInfoUserDto) : updateUser(fullInfoUserDto);
  }

  @Override
  public void changeEmployeeAccountStatus(EmployeeStatusDto employeeStatusDto) {
    List<UserRepresentation> userRepresentations = realmResource.users()
        .search(employeeStatusDto.getUsername(), null, null, null, null, null, null, null);

    userRepresentations.stream()
        .findFirst()
        .ifPresent(userRepresentation -> {
          userRepresentation.setEnabled(employeeStatusDto.isEnabled());
          realmResource.users().get(userRepresentation.getId()).update(userRepresentation);
        });
  }

  @Override
  public boolean isPasswordCorrect(String username, String password) {
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
      throw new WrongPasswordException(WRONG_PASSWORD_MESSAGE, password, WRONG_PASSWORD_I18N_PLACEHOLDER);
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
      throw new WrongPasswordException(WRONG_PASSWORD_MESSAGE, passwordDto.getUsername(), WRONG_PASSWORD_I18N_PLACEHOLDER);
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

  private Long createUser(FullInfoUserDto fullInfoUserDto) {
    User user = userService.saveUser(fullInfoUserDto);
    if (CUSTOMER.value.equals(fullInfoUserDto.getRole())) {
      creditCardService.saveCreditCard(user);
    }

    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(fullInfoUserDto.getUsername());
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
    credentialRepresentation.setValue(fullInfoUserDto.getPassword());
    userResource.resetPassword(credentialRepresentation);

    RoleRepresentation roleRepresentation = realmResource.roles().get(fullInfoUserDto.getRole())
        .toRepresentation();
    userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));

    return user.getId();
  }

  private String getCreatedId(Response response) {
    URI location = response.getLocation();
    if (!response.getStatusInfo().equals(Status.CREATED)) {
      StatusType statusInfo = response.getStatusInfo();
      throw new WebApplicationException("Create method returned status " +
          statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode()
          + "); expected status: Created (201)", response);
    }
    if (location == null) {
      return null;
    }
    String path = location.getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }

  private Long updateUser(FullInfoUserDto fullInfoUserDto) {
    User user = userService.saveUser(fullInfoUserDto);

    List<UserRepresentation> userRepresentations = realmResource.users()
        .search(fullInfoUserDto.getUsername(), null, null, null, null, null, null, null);

    userRepresentations.stream()
        .findFirst()
        .ifPresent(userRepresentation -> {
          userRepresentation.setFirstName(user.getFirstName());
          userRepresentation.setLastName(user.getLastName());
          userRepresentation.setEmail(user.getEmail());
          userRepresentation.singleAttribute("userId", String.valueOf(user.getId()));
          userRepresentation.setEnabled(fullInfoUserDto.isEnabled());
          realmResource.users().get(userRepresentation.getId()).update(userRepresentation);
        });
    String userId = userRepresentations.get(0).getId();
    UserResource userResource = realmResource.users().get(userId);

    if (fullInfoUserDto.getPassword() != null) { // only update password when necessary
      CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
      credentialRepresentation.setTemporary(false);
      credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
      credentialRepresentation.setValue(fullInfoUserDto.getPassword());
      userResource.resetPassword(credentialRepresentation);
    }

    RoleRepresentation roleRepresentation = realmResource
        .roles()
        .get(fullInfoUserDto.getRole())
        .toRepresentation();
    userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));

    return user.getId();
  }

  @Override
  public Set<UserRepresentation> getAllEmployees() {
    Set<UserRepresentation> userRepresentations = realmResource
        .roles()
        .get(EMPLOYEE.value)
        .getRoleUserMembers();

    return userRepresentations;
  }

  @Override
  public UserRepresentation getEmployeeById(Long id) {
    Set<UserRepresentation> userRepresentations = realmResource
        .roles()
        .get(EMPLOYEE.value)
        .getRoleUserMembers();

    return userRepresentations.stream()
        .filter(userRepresentation -> userRepresentation.getAttributes().get("userId").get(0).equals(String.valueOf(id)))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE, id.toString(), USER_NOT_FOUND));
  }

  @Override
  public Set<UserRepresentation> getAllCustomers() {
    Set<UserRepresentation> userRepresentations = realmResource
        .roles()
        .get(CUSTOMER.value)
        .getRoleUserMembers();

    return userRepresentations;
  }

  @Override
  public UserRepresentation getCustomerById(Long id) {
    Set<UserRepresentation> userRepresentations = realmResource
        .roles()
        .get(CUSTOMER.value)
        .getRoleUserMembers();

    return userRepresentations.stream()
        .filter(userRepresentation -> userRepresentation.getAttributes().get("userId").get(0).equals(String.valueOf(id)))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE, id.toString(), USER_NOT_FOUND));
  }
}
