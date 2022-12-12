package org.hcmus.premiere.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "credit_card", schema = "premiere")
@Data
public class CreditCard extends PremiereAbstractEntity {

  @Basic
  @Column(name = "balance", nullable = false, columnDefinition = "NUMERIC")
  private BigDecimal balance;

  @Basic
  @Column(name = "open_day", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime openDay;

  @Basic
  @Column(name = "card_number", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
  private String cardNumber;

  @JsonIgnore
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "senderCreditCard")
  private Set<LoanReminder> senderLoanReminders;

  @OneToMany(mappedBy = "receiverCreditCard")
  private Set<LoanReminder> receiverLoanReminders;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditCard that = (CreditCard) o;
    return Objects.equals(id, that.id)
        && Objects.equals(version, that.version)
        && Objects.equals(balance, that.balance)
        && Objects.equals(openDay, that.openDay)
        && Objects.equals(cardNumber, that.cardNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, balance, openDay, cardNumber, version);
  }
}
