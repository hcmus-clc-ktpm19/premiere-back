package org.hcmus.premiere.model.entity;

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "otp", schema = "premiere")
@Data
public class OTP extends PremiereAbstractEntity {

  @Basic
  @Column(name = "otp", nullable = false, columnDefinition = "VARCHAR(255)")
  private String otp;
  @Basic
  @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255)")
  private String email;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OTP otp1 = (OTP) o;
    return Objects.equals(id, otp1.id) && Objects.equals(version, otp1.version) && Objects.equals(otp1.otp,
        otp1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, otp, version);
  }
}
