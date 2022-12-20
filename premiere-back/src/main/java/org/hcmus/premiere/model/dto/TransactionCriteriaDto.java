package org.hcmus.premiere.model.dto;

import javax.validation.constraints.Min;
import lombok.Data;
import org.hcmus.premiere.model.enums.TransactionType;

@Data
public class TransactionCriteriaDto {

  @Min(0)
  private int page;
  @Min(0)
  private int size = 9;
  private TransactionType transactionType;
  private boolean isAsc;
}
