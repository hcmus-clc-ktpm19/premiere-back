package org.hcmus.premiere.model.dto;

import java.time.LocalDate;
import javax.validation.constraints.Min;
import lombok.Data;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.Nullable;

@Data
public class TransactionCriteriaDto {

  @Min(0)
  @Nullable
  private int page;
  @Min(0)
  @Nullable
  private int size = 9;
  @Nullable
  private TransactionType transactionType;
  @Nullable
  private boolean isAsc;
  @Nullable
  private MoneyTransferCriteria moneyTransferCriteria;
  @Nullable
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate fromDate;
  @Nullable
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate toDate;
}
