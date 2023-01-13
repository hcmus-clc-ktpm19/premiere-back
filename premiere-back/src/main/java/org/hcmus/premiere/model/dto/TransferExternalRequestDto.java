package org.hcmus.premiere.model.dto;

import lombok.Data;
import org.hcmus.premiere.model.entity.CreditCard;

/**
 * A DTO for the {@link CreditCard} entity
 */
@Data
public class TransferExternalRequestDto {
  private String accountNumber;
  private TransactionInfoExternalDto transactionInfo;
  private long timestamp;
  private String msgToken;
  private String slug;
  private String signature;
}