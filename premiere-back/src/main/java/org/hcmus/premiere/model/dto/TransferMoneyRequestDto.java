package org.hcmus.premiere.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.util.validation.ValueOfEnum;


@Data
public class TransferMoneyRequestDto {
  @NotNull(message = "Checking Transaction Id is mandatory")
  private Long checkingTransactionId;
  @NotBlank(message = "OTP code is mandatory")
  private String otp;
}

