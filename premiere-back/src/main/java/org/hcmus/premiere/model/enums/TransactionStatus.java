package org.hcmus.premiere.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionStatus {
  CHECKING("CHECKING"), COMPLETED("COMPLETED");

  final String value;
}
