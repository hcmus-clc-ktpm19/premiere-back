package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class WrongPasswordException extends RuntimeException {

  public static final String WRONG_PASSWORD_MESSAGE = "Wrong password exception";
  private final String identify;

  public WrongPasswordException(String message, String identify) {
    super(message);
    this.identify = identify;
  }
}
