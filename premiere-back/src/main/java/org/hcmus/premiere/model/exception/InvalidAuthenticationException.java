package org.hcmus.premiere.model.exception;

public class InvalidAuthenticationException extends RuntimeException {

  public InvalidAuthenticationException() {
    super("invalid username or password");
  }
}
