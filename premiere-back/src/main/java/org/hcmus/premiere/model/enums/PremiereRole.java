package org.hcmus.premiere.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PremiereRole {

  CUSTOMER("CUSTOMER"),
  EMPLOYEE("EMPLOYEE"),
  PREMIERE_ADMIN("PREMIERE_ADMIN");

  public final String value;
}
