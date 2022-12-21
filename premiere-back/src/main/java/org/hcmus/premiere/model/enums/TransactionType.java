package org.hcmus.premiere.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {
  LOAN("LOAN"),
  MONEY_TRANSFER("MONEY_TRANSFER");
  final String value;
}
