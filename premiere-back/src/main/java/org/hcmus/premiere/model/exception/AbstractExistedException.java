package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public abstract class AbstractExistedException extends PremiereAbstractException {

  private final String identify;

  AbstractExistedException(String message, String identify, String i18nPlaceHolder) {
    super(message, i18nPlaceHolder);
    this.identify = identify;
  }
}
