package org.hcmus.premiere.model.dto;

import lombok.Data;
import org.hcmus.premiere.model.entity.CreditCard;

/**
 * A DTO for the {@link CreditCard} entity
 */
@Data
public class TransferResponseDataDto {
  private String accountDesNumber;
  private long amount;
  private String description;
  private String payTransactionFee;
  private int status;
  private String accountSrcNumber;
  private String transactionType;
  private long bankDesId;
  private long id;
}