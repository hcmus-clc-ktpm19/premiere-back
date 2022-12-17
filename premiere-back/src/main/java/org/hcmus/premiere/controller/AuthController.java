package org.hcmus.premiere.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.hcmus.premiere.model.dto.PasswordDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.service.KeycloakService;
import org.hcmus.premiere.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  private final KeycloakService keycloakService;

  @GetMapping("/token/user")
  public UserDto getUserByToken() {
    UserRepresentation userRepresentation = keycloakService.getCurrentUser();
    User user = userService.findUserById(Long.valueOf(userRepresentation.getAttributes().get("userId").get(0)));

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

  @PostMapping("/register")
  @RolesAllowed("EMPLOYEE")
  public ResponseEntity register(@RequestBody @Valid RegisterAccountDto registerAccountDto) {
    keycloakService.createUser(registerAccountDto);
    return ResponseEntity.status(201).body("Register successfully");
  }
}
