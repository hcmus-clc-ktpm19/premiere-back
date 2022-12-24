package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OTPDto implements Serializable {
  private String otp;
  private String email;
  private LocalDateTime createdAt;
}
