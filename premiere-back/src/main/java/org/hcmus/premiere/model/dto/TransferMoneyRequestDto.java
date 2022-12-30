package org.hcmus.premiere.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;


@Data
public class TransferMoneyRequestDto {
  @NotNull(message = "Request Transaction Id is mandatory")
    private Long requestID;
  @NotBlank(message = "OTP code is mandatory")
  private String otp;
}

