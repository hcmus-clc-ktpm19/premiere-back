package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class BankNotFoundException extends AbstractNotFoundException {

  public static final String BANK_NOT_FOUND_MESSAGE = "Bank not found";

  public BankNotFoundException(String message, String id) {
    super(message, id);
  }

}
