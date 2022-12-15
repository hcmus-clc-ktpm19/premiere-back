package org.hcmus.premiere.model.dto;

import lombok.Data;

@Data
public class PasswordDto {
  private String username;
  private String currentPassword;
  private String newPassword;
}
