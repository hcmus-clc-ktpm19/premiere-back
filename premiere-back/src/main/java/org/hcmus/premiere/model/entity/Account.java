package org.hcmus.premiere.model.entity;

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
