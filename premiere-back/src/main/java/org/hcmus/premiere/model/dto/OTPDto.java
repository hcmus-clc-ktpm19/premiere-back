package org.hcmus.premiere.model.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OTPDto {
  private String otp;
  private String email;
  private LocalDateTime createdAt;
}
