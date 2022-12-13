package org.hcmus.premiere.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "receiver", schema = "premiere")
@Data
public class Receiver extends PremiereAbstractEntity {

  @Basic
  @Column(name = "card_number", nullable = false, columnDefinition = "VARCHAR(255)")
  private String cardNumber;

  @Basic
  @Column(name = "nickname", nullable = false, columnDefinition = "VARCHAR(255)")
  private String nickname;

  @Basic
  @Column(name = "full_name", nullable = false, columnDefinition = "VARCHAR(255)")
  private String fullName;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

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
        && Objects.equals(version, receiver.version) && Objects.equals(cardNumber, receiver.cardNumber)
        && Objects.equals(nickname, receiver.nickname) && Objects.equals(fullName,
        receiver.fullName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cardNumber, nickname, fullName, version);
  }
}
