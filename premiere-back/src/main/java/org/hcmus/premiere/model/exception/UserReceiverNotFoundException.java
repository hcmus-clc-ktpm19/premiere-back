package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class UserReceiverNotFoundException extends AbstractNotFoundException {

  public static final String USER_RECEIVER_NOT_FOUND_MESSAGE = "User or receiver not found";
  public static final String USER_RECEIVER_NOT_FOUND = "USER_RECEIVER.NOT_FOUND";

  public UserReceiverNotFoundException(String message, String identify, String i18nPlaceHolder) {
    super(message, identify, i18nPlaceHolder);
  }
}
