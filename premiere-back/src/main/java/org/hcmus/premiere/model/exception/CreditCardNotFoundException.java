package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class CreditCardNotFoundException extends AbstractNotFoundException {

  public CreditCardNotFoundException(String message, String id) {
    super(message, id);
  }
}
