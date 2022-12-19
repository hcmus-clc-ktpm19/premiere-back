package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class ReceiverNotFoundException extends AbstractNotFoundException {

  public static final String RECEIVER_NOT_FOUND_MESSAGE = "Receiver not found";

  public static final String RECEIVER_NOT_FOUND = "RECEIVER.NOT_FOUND";

  public ReceiverNotFoundException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }

}
