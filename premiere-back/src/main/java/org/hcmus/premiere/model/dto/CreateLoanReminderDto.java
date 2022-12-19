package org.hcmus.premiere.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateLoanReminderDto {

  @NotEmpty
  @NotBlank
  private String creditorCreditCardNumber;

  @NotEmpty
  @NotBlank
  private String debtorCreditCardNumber;

  @NotEmpty
  @NotBlank
  private String debtorName;

  @Min(100_000)
  private BigDecimal transferAmount;

  private String loanRemark;

  @Pattern(regexp = "\\(?(\\d{3})\\)?([ .-]?)(\\d{3})\\2(\\d{4})")
  private String debtorPhone;
}
