package org.hcmus.premiere.model.enums;

public enum CardStatus {

  AVAILABLE("AVAILABLE"),
  DISABLED("DISABLED");

  private final String value;

  CardStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
