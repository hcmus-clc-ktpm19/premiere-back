package org.hcmus.premiere.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.model.enums.TransactionType;

@Entity
@Table(name = "transaction", schema = "premiere")
@Data
public class Transaction extends PremiereAbstractEntity {

  @Basic
  @Column(name = "amount", nullable = false, columnDefinition = "NUMERIC")
  private BigDecimal amount;

  @Basic
  @Column(name = "type", nullable = false, columnDefinition = "TRANSACTION_TYPE")
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @Basic
  @Column(name = "time", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime time;

  @Basic
  @Column(name = "transaction_remark", nullable = false, columnDefinition = "VARCHAR(255)")
  private String transactionRemark;

  @Basic
  @Column(name = "sender_balance", nullable = false, columnDefinition = "NUMERIC")
  private BigDecimal senderBalance;

  @Basic
  @Column(name = "receiver_balance", nullable = false, columnDefinition = "NUMERIC")
  private BigDecimal receiverBalance;

  @Basic
  @Column(name = "sender_credit_card_number", nullable = false, columnDefinition = "VARCHAR(255)")
  private String  senderCreditCardNumber;

  @Basic
  @Column(name = "receiver_credit_card_number", nullable = false, columnDefinition = "VARCHAR(255)")
  private String receiverCreditCardNumber;

  @Basic
  @Column(name = "fee", nullable = false, columnDefinition = "NUMERIC")
  private BigDecimal fee;

  @Basic
  @Column(name = "is_self_payment_fee", nullable = false, columnDefinition = "BOOLEAN")
  private boolean isSelfPaymentFee;

  @Basic
  @Column(name = "status", nullable = false, columnDefinition = "TRANSACTION_STATUS")
  @Enumerated(EnumType.STRING)
  private TransactionStatus status;

  @ManyToOne
  @JoinColumn(name = "sender_bank_id", referencedColumnName = "id", nullable = false)
  private Bank senderBank;

  @ManyToOne
  @JoinColumn(name = "receiver_bank_id", referencedColumnName = "id", nullable = false)
  private Bank receiverBank;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction that = (Transaction) o;
    return Objects.equals(id, that.id) && Objects.equals(that.amount, amount)
        && Objects.equals(receiverCreditCardNumber, that.receiverCreditCardNumber)
        && Objects.equals(that.fee, fee) && isSelfPaymentFee == that.isSelfPaymentFee
        && Objects.equals(version, that.version) && Objects.equals(type, that.type)
        && Objects.equals(time, that.time) && Objects.equals(transactionRemark,
        that.transactionRemark) && Objects.equals(senderBalance, that.senderBalance)
        && Objects.equals(status, that.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, type, time, transactionRemark, senderBalance,
        receiverCreditCardNumber, fee, isSelfPaymentFee, status,
        version);
  }
}
