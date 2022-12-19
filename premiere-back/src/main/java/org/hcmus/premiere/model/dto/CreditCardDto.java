package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.hcmus.premiere.model.entity.CreditCard;

/**
 * A DTO for the {@link CreditCard} entity
 */
@Data
public class CreditCardDto implements
    Serializable {

  private Long id;
  private Long version;
  private BigDecimal balance;
  private LocalDateTime openDay;
  private String cardNumber;
}