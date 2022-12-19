package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends AbstractNotFoundException {

  public static final String USER_NOT_FOUND_MESSAGE = "User not found";

  public static final String USER_NOT_FOUND = "AUTH.USER.NOT_FOUND";

  public UserNotFoundException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }
}
