package org.hcmus.premiere.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {

  MONEY_TRANSFER("MONEY_TRANSFER"),
  LOAN("LOAN");

  final String value;
}
