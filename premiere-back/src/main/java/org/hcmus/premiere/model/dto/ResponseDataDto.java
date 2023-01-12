package org.hcmus.premiere.model.dto;

import java.util.Map;
import lombok.Data;
import org.hcmus.premiere.model.entity.CreditCard;

/**
 * A DTO for the {@link CreditCard} entity
 */
@Data
public class ResponseDataDto {
  private String accountNumber;
  private Map<String, String> user;
}