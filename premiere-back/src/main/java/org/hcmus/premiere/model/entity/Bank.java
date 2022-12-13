package org.hcmus.premiere.model.entity;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bank", schema = "premiere")
@Data
public class Bank extends PremiereAbstractEntity {

  @Basic
  @Column(name = "bank_name", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
  private String bankName;

  @OneToMany(mappedBy = "bank")
  private Set<Receiver> receivers;

  @OneToMany(mappedBy = "senderBank")
  private Set<Transaction> senderTransactions;

  @OneToMany(mappedBy = "receiverBank")
  private Set<Transaction> receiverTransactions;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Bank bank = (Bank) o;
    return Objects.equals(id, bank.id) && Objects.equals(version, bank.version) && Objects.equals(bankName,
        bank.bankName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bankName, version);
  }
}
