package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public abstract class AbstractExistedException extends RuntimeException {
  private final String identify;

  AbstractExistedException(String message, String identify) {
    super(message);
    this.identify = identify;
  }
}
