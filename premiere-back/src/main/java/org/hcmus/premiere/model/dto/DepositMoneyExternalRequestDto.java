package org.hcmus.premiere.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositMoneyExternalRequestDto implements Serializable {
  private String senderCreditCardNumber;
  private String receiverCreditCardNumber;
  private BigDecimal amount;
  private String senderBankName;
  private String rsaToken;
}

