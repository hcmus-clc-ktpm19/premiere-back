package org.hcmus.premiere.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MoneyTransferCriteria {

  INCOMING("INCOMING"),
  OUTGOING("OUTGOING");

  final String value;
}
