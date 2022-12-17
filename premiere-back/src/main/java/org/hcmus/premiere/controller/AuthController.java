package org.hcmus.premiere.controller;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.OTPDto;
import org.hcmus.premiere.model.dto.PasswordDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.service.OTPService;
import org.hcmus.premiere.service.KeycloakService;
import org.hcmus.premiere.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

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
    Boolean verifyOTP = otpService.verifyOTP(otpDto.getOtp(), otpDto.getEmail());
    if (verifyOTP) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }
}
