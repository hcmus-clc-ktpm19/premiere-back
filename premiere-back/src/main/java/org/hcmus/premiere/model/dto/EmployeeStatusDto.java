package org.hcmus.premiere.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeStatusDto {
  @NotBlank(message = "Username may not be blank")
  private String username;


  private boolean enabled;
}
