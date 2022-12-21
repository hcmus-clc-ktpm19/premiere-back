package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.model.enums.LoanStatus;

/**
 * A DTO for the {@link LoanReminder} entity
 */
@Data
public class LoanReminderDto implements Serializable {

  private Long id;
  private Long version;
  private BigDecimal transferAmount;
  private LoanStatus status;
  private LocalDateTime time;
  private String loanRemark;
  private String senderCreditCardNumber;
  private String senderName;
  private String receiverCreditCardNumber;
  private String receiverName;
}