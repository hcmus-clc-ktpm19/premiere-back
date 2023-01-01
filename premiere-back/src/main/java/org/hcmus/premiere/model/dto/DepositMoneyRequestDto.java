package org.hcmus.premiere.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositMoneyRequestDto {

  private String username;
  private String creditCardNumber;
  private BigDecimal amount;
}
