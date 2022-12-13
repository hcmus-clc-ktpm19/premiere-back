package org.hcmus.premiere.model.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Data;
import org.hcmus.premiere.model.enums.Gender;

@Entity
@Table(name = "user", schema = "premiere")
@Data
public class User extends PremiereAbstractEntity {

  @Basic
  @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(255)", length = 255)
  private String firstName;

  @Basic
  @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(255)", length = 255)
  private String lastName;

  @Basic
  @Column(name = "dob", columnDefinition = "DATE")
  private LocalDate dob;

  @Basic
  @Column(name = "gender", nullable = false, columnDefinition = "GENDER")
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Basic
  @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255)")
  @Email
  private String email;

  @Basic
  @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(10)", length = 10)
  private String phone;

  @Basic
  @Column(name = "pan_number", nullable = false, columnDefinition = "VARCHAR(255)")
  private String panNumber;

  @Basic
  @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(255)")
  private String address;

  @Basic
  @Column(name = "avatar", columnDefinition = "VARCHAR(255)", nullable = false)
  private String avatar;

  @OneToOne(mappedBy = "user")
  private CreditCard creditCards;

  @OneToMany(mappedBy = "user")
  private Set<Receiver> receivers;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(version, user.version) && Objects.equals(
        firstName,
        user.firstName) && Objects.equals(lastName, user.lastName)
        && Objects.equals(dob, user.dob) && gender == user.gender
        && Objects.equals(email, user.email) && Objects.equals(phone, user.phone)
        && Objects.equals(panNumber, user.panNumber) && Objects.equals(address,
        user.address) && Objects.equals(avatar, user.avatar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, firstName, lastName, dob, gender, email, phone,
        panNumber, address, avatar, version);
  }
}
