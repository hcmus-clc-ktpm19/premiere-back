package org.hcmus.premiere.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hcmus.premiere.model.enums.Gender;
import org.hcmus.premiere.model.enums.PremiereRole;
import org.hcmus.premiere.util.validation.ValueOfEnum;

@Data
public class RegisterAccountDto {
  @NotBlank(message = "Username may not be blank")
  private String username;
  @NotBlank(message = "Email may not be blank")
  private String email;
  @NotBlank(message = "First name may not be blank")
  private String firstName;
  @NotBlank(message = "Last name may not be blank")
  private String lastName;
  @NotBlank(message = "Phone may not be blank")
  private String phone;
  @ValueOfEnum(enumClass = Gender.class)
  @NotBlank(message = "Gender may not be blank")
  private String gender;
  @NotBlank(message = "Identification number may not be blank")
  private String panNumber;
  @NotBlank(message = "Address may not be blank")
  private String address;
  @NotBlank(message = "Password may not be blank")
  private String password;
  @ValueOfEnum(enumClass = PremiereRole.class)
  @NotBlank(message = "Role may not be blank")
  private String role;
}
