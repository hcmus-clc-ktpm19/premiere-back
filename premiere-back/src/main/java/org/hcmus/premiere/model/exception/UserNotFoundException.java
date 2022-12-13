package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends AbstractNotFoundException {

  public static final String USER_NOT_FOUND_MESSAGE = "User not found";

  public UserNotFoundException(String message, String id) {
    super(message, id);
  }
}
