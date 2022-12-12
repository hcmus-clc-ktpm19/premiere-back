package org.hcmus.premiere.controller;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.service.KeycloakService;
import org.hcmus.premiere.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
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
    User user = userService.getUserById(Long.valueOf(userRepresentation.getAttributes().get("userId").get(0)));

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
}
