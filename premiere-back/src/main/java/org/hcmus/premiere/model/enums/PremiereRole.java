package org.hcmus.premiere.model.enums;

public enum PremiereRole {

  CUSTOMER("CUSTOMER"), EMPLOYEE("EMPLOYEE"), PREMIERE_ADMIN("PREMIERE_ADMIN");

  private final String value;

  PremiereRole(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
