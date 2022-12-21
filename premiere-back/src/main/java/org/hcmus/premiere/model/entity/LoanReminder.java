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
import lombok.ToString;
import org.hcmus.premiere.model.enums.LoanStatus;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "loan_reminder", schema = "premiere")
@Data
public class LoanReminder extends PremiereAbstractEntity {

  @Basic
  @Column(name = "loan_balance", nullable = false, columnDefinition = "NUMERIC")
  private BigDecimal loanBalance;

  @Basic
  @Column(name = "status", nullable = false, columnDefinition = "LOAN_STATUS")
  @Enumerated(EnumType.STRING)
  private LoanStatus status = LoanStatus.PENDING;

  @Basic
  @Column(name = "time", nullable = false, columnDefinition = "TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime time;

  @Basic
  @Column(name = "loan_remark", nullable = false, columnDefinition = "VARCHAR(255)")
  private String loanRemark;

  @ManyToOne
  @JoinColumn(name = "sender_credit_card_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private CreditCard senderCreditCard;

  @ManyToOne
  @JoinColumn(name = "receiver_credit_card_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private CreditCard receiverCreditCard;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoanReminder that = (LoanReminder) o;
    return Objects.equals(id, that.id)
        && Objects.equals(
        version, that.version)
        && Objects.equals(loanBalance, that.loanBalance) && Objects.equals(status,
        that.status) && Objects.equals(time, that.time) && Objects.equals(
        loanRemark, that.loanRemark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, loanBalance, status, time,
        loanRemark, version);
  }
}
