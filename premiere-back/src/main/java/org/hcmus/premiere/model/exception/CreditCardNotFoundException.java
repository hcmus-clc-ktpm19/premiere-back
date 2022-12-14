package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class CreditCardNotFoundException extends AbstractNotFoundException {

  public static final String CREDIT_CARD_NOT_FOUND = "CREDIT_CARD.NOT_FOUND";

  public CreditCardNotFoundException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }
}
