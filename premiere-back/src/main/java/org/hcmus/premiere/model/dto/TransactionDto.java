package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.model.enums.TransactionType;

/**
 * A DTO for the {@link org.hcmus.premiere.model.entity.Transaction} entity
 */
@Data
public class TransactionDto implements Serializable {

  private Long id;
  private Long version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private BigDecimal amount;
  private TransactionType type;
  private LocalDateTime time;
  private String transactionRemark;
  private BigDecimal totalBalance;
  private String senderCreditCardNumber;
  private String receiverCreditCardNumber;
  private BigDecimal fee;
  private boolean isSelfPaymentFee;
  private TransactionStatus status;
  private Long senderBankId;
  private Long receiverBankId;
}