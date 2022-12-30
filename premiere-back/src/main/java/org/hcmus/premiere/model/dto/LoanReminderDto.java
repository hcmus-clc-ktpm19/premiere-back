package org.hcmus.premiere.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime time;
  private String loanRemark;
  private Long senderCreditCardId;
  private Long receiverCreditCardId;
  private String senderCreditCardNumber;
  private String senderName;
  private Long senderId;
  private String receiverCreditCardNumber;
  private String receiverName;
  private Long receiverId;
  private String cancelReason;
}