package org.hcmus.premiere.model.dto;

import lombok.Data;
import org.hcmus.premiere.model.entity.CreditCard;

/**
 * A DTO for the {@link CreditCard} entity
 */
@Data
public class TransferExternalResponseDto {
  private TransferResponseDataDto data;
  private int statusCode;
  private String signature;
  private String message;
}