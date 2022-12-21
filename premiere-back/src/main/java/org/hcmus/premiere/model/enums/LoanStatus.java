package org.hcmus.premiere.model.enums;

public enum LoanStatus {

  APPROVED("APPROVED"), PENDING("PENDING"), REJECTED("REJECTED"), CANCELLED("CANCELLED");

  private final String value;

  LoanStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
