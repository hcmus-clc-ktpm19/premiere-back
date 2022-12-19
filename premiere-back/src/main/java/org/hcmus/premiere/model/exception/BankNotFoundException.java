package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class BankNotFoundException extends AbstractNotFoundException {

  public static final String BANK_NOT_FOUND_MESSAGE = "Bank not found";

  public static final String BANK_NOT_FOUND = "BANK.NOT_FOUND";

  public BankNotFoundException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }

}
