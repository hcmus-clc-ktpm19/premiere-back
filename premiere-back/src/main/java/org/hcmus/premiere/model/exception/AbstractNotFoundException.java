package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public abstract class AbstractNotFoundException extends RuntimeException {

  private final String identify;

  private final String i18nPlaceHolder;

  AbstractNotFoundException(String message, String identify, String i18nPlaceHolder) {
    super(message);
    this.identify = identify;
    this.i18nPlaceHolder = i18nPlaceHolder;
  }
}
