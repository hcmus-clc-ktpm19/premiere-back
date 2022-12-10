package org.hcmus.premiere.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Data;
import org.hcmus.premiere.model.enums.PremiereRole;

@Entity
@Table(name = "account", schema = "premiere")
@Data
public class Account extends PremiereAbstractEntity {

  @Basic
  @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
  private String username;

  @Basic
  @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
  private String password;

  @Basic
  @Column(name = "role", nullable = false, columnDefinition = "PREMIERE_ROLE")
  @Enumerated(EnumType.STRING)
  private PremiereRole role;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(id, account.id)
        && Objects.equals(version,
        account.version)
        && Objects.equals(username, account.username) && Objects.equals(password,
        account.password) && Objects.equals(role, account.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, role, version);
  }
}
