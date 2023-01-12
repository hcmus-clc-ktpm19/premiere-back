package org.hcmus.premiere.model.dto;

import lombok.Data;
import org.hcmus.premiere.model.entity.CreditCard;

/**
 * A DTO for the {@link CreditCard} entity
 */
@Data
public class CreditCardExternalDto {
  private String accountNumber;
  private long timestamp;
  private String msgToken;
  private String slug;
}