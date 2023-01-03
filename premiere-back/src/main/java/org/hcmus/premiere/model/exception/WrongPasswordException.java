package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class WrongPasswordException extends PremiereAbstractException {

  public static final String WRONG_PASSWORD_MESSAGE = "Wrong password exception";

  public static final String WRONG_PASSWORD_I18N_PLACEHOLDER = "AUTH.WRONG_PASSWORD";

  private final String identify;

  public WrongPasswordException(String message, String identify, String i18nPlaceHolder) {
    super(message, i18nPlaceHolder);
    this.identify = identify;
  }
}
