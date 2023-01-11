package org.hcmus.premiere.model.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "receiver", schema = "premiere")
@Data
public class Receiver extends PremiereAbstractEntity {

  @Basic
  @Column(name = "card_number", nullable = false, columnDefinition = "VARCHAR(255)")
  private String cardNumber;

  @OneToMany(
      mappedBy = "receiver",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<UserReceiver> users = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "bank_id", referencedColumnName = "id", nullable = false)
  private Bank bank;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Receiver receiver = (Receiver) o;
    return Objects.equals(id, receiver.id)
        && Objects.equals(version, receiver.version) && Objects.equals(cardNumber, receiver.cardNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cardNumber, version);
  }
}
