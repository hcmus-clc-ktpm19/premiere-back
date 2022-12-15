package org.hcmus.premiere.model.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreditCardDto {
  private String cardNumber;
  private BigDecimal balance;
}
