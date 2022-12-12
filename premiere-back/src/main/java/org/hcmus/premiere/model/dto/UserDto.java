package org.hcmus.premiere.model.dto;

import lombok.Data;
import org.hcmus.premiere.model.enums.PremiereRole;

@Data
public class UserDto {
  private Long id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String phone;
  private PremiereRole role;
}
