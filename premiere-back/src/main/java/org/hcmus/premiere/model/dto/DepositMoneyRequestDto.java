package org.hcmus.premiere.model.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DepositMoneyRequestDto {

  private String phone;
  private String creditCardNumber;
  private BigDecimal amount;
}
