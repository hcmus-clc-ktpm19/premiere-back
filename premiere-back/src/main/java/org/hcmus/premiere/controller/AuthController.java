package org.hcmus.premiere.controller;

import static org.hcmus.premiere.model.enums.PremiereRole.CUSTOMER;
import static org.hcmus.premiere.model.enums.PremiereRole.EMPLOYEE;
import static org.springframework.http.HttpStatus.CREATED;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.common.consts.PremiereApiUrls;
import org.hcmus.premiere.model.dto.EmployeeStatusDto;
import org.hcmus.premiere.model.dto.FullInfoUserDto;
import org.hcmus.premiere.model.dto.OTPDto;
import org.hcmus.premiere.model.dto.PasswordDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.service.KeycloakService;
import org.hcmus.premiere.service.OTPService;
import org.hcmus.premiere.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PremiereApiUrls.PREMIERE_API_V1 + "/auth")
@RequiredArgsConstructor
public class AuthController extends AbstractApplicationController {

  private final UserService userService;

  private final KeycloakService keycloakService;

  private final OTPService otpService;

  @GetMapping("/token/user")
  public UserDto getUserByToken() {
    UserRepresentation userRepresentation = keycloakService.getCurrentUser();
    User user = userService.findUserById(
        Long.valueOf(userRepresentation.getAttributes().get("userId").get(0)));

    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setFirstName(user.getFirstName());
    userDto.setLastName(user.getLastName());
    userDto.setEmail(user.getEmail());
    userDto.setPhone(user.getPhone());
    userDto.setUsername(userRepresentation.getUsername());
    userDto.setRole(PremiereRole.valueOf(keycloakService.getCurrentUserRole().getName()));
    return userDto;
  }

  @PutMapping("/change-password")
  public ResponseEntity<?> changePassword(@RequestBody PasswordDto passwordDto) {
    keycloakService.changePassword(passwordDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register-customer")
  public ResponseEntity<Map<String, String>> registerCustomer(@RequestBody @Valid FullInfoUserDto fullInfoUserDto) {
    keycloakService.createCustomer(fullInfoUserDto);
    Map<String, String> response = new HashMap<>();
    response.put("code", "201");
    response.put("message", "Register successfully");
    return ResponseEntity.status(CREATED).body(response);
  }

  @PostMapping("/save-customer")
  public ResponseEntity<Long> saveCustomer(@RequestBody @Valid FullInfoUserDto fullInfoUserDto) {
    Long customerId = keycloakService.saveCustomer(fullInfoUserDto);
    return ResponseEntity.status(CREATED).body(customerId);
  }

  @PostMapping("/save-employee")
  public ResponseEntity<Long> saveEmployee(@RequestBody @Valid FullInfoUserDto fullInfoUserDto) {
    Long employeeId = keycloakService.saveEmployee(fullInfoUserDto);
    return ResponseEntity.status(CREATED).body(employeeId);
  }

  @GetMapping("/get-employees/{id}")
  public ResponseEntity<FullInfoUserDto> getEmployeeById(@PathVariable Long id) {
    UserRepresentation userRepresentation = keycloakService.getEmployeeById(id);
    User user = userService.findUserById(Long.valueOf(userRepresentation.getAttributes().get("userId").get(0)));
    return ResponseEntity.ok().body(applicationMapper.toFullInfoUserDto(user, userRepresentation, EMPLOYEE.value));
  }

  @GetMapping("/get-employees")
  public ResponseEntity<List<FullInfoUserDto>> getAllEmployees() {
    Set<UserRepresentation> userRepresentations = keycloakService.getAllEmployees();
    List<FullInfoUserDto> employees = userRepresentations.stream()
        .map(userRepresentation -> {
          User user = userService.findUserById(
              Long.valueOf(userRepresentation.getAttributes().get("userId").get(0)));

          return applicationMapper.toFullInfoUserDto(user, userRepresentation, EMPLOYEE.value);
        })
        .toList();
    return ResponseEntity.ok().body(employees);
  }

  @GetMapping("/get-customers/{id}")
  public ResponseEntity<FullInfoUserDto> getCustomerById(@PathVariable Long id) {
    UserRepresentation userRepresentation = keycloakService.getCustomerById(id);
    User user = userService.findUserById(Long.valueOf(userRepresentation.getAttributes().get("userId").get(0)));

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(applicationMapper.toFullInfoUserDto(user, userRepresentation, CUSTOMER.value));
  }

  @GetMapping("/get-customers")
  public ResponseEntity<List<FullInfoUserDto>> getAllCustomers() {
    Set<UserRepresentation> userRepresentations = keycloakService.getAllCustomers();
    List<FullInfoUserDto> customers = userRepresentations.stream()
        .map(userRepresentation -> {
          User user = userService.findUserById(
              Long.valueOf(userRepresentation.getAttributes().get("userId").get(0)));

          return applicationMapper.toFullInfoUserDto(user, userRepresentation, CUSTOMER.value);
        })
        .collect(Collectors.toList());
    return ResponseEntity.ok().body(customers);
  }

  @PostMapping("/change-employee-status")
  public ResponseEntity<?> changeEmployeeAccountStatus(@RequestBody EmployeeStatusDto employeeStatusDto) {
    keycloakService.changeEmployeeAccountStatus(employeeStatusDto);
    return ResponseEntity.ok().build();
  }


  @PostMapping("/request-otp")
  public ResponseEntity<?> requestOTP(@RequestBody OTPDto otpDto) {
    otpService.sendOTPEmail(otpDto.getEmail());
    return ResponseEntity.ok().build();
  }

  @PutMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody PasswordDto passwordDto) {
    keycloakService.resetPassword(passwordDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/verify-otp")
  public ResponseEntity<?> verifyOTP(@RequestBody OTPDto otpDto) {
    boolean verifyOTP = otpService.verifyOTP(otpDto.getOtp(), otpDto.getEmail());
    return verifyOTP ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
  }
}
