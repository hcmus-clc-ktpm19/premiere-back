package org.hcmus.premiere.model.exception;

public class ReceiverExistedException extends AbstractExistedException{
  public static final String RECEIVER_EXISTED_MESSAGE = "Receiver already existed";

  public static final String RECEIVER_EXISTED_I18N_PLACEHOLDER = "RECEIVER.EXISTED";

  public ReceiverExistedException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }
}
