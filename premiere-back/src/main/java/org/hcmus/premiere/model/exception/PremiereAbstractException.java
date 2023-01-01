package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public abstract class PremiereAbstractException extends RuntimeException {

  protected final String i18nPlaceHolder;

  public static final String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";

  PremiereAbstractException(String message, String i18nPlaceHolder) {
    super(message);
    this.i18nPlaceHolder = i18nPlaceHolder;
  }
}
