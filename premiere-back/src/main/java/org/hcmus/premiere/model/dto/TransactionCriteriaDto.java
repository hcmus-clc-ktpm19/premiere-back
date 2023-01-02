package org.hcmus.premiere.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import lombok.Data;
import org.hcmus.premiere.model.enums.TransactionType;
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
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate fromDate;
  @Nullable
  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate toDate;
}
