package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class ReceiverExisted extends AbstractExistedException {

  public static final String RECEIVER_EXISTED_MESSAGE = "Receiver already existed";
  public static final String RECEIVER_EXISTED = "RECEIVER.EXISTED";

  public ReceiverExisted(String message, String identify, String i18nPlaceHolder) {
    super(message, identify, i18nPlaceHolder);
  }
}
