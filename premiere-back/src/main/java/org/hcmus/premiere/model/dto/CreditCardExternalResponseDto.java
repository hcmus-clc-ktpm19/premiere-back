package org.hcmus.premiere.model.dto;

import lombok.Data;
import org.hcmus.premiere.model.entity.CreditCard;

/**
 * A DTO for the {@link CreditCard} entity
 */
@Data
public class CreditCardExternalResponseDto {
  private ResponseDataDto data;
  private int statusCode;
  private String message;
}