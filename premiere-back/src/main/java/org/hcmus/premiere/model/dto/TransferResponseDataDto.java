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
  private int status;
  private String accountSrcNumber;
  private long userId;
  private String transactionType;
  private long id;
}