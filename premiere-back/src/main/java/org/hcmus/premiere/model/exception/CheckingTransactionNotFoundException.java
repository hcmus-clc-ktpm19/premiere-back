package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class CheckingTransactionNotFoundException extends AbstractNotFoundException {

  public static final String CHECKING_TRANSACTION_NOT_FOUND_MESSAGE = "Checking transaction not found";

  public static final String CHECKING_TRANSACTION_NOT_FOUND = "CHECKING_TRANSACTION.NOT_FOUND";

  public CheckingTransactionNotFoundException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }

}
