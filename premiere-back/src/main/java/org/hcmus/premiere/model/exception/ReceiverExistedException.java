package org.hcmus.premiere.model.exception;

public class ReceiverExistedException extends AbstractExistedException{
  public static final String RECEIVER_EXISTED_MESSAGE = "Receiver already existed";

  public ReceiverExistedException(String message, String id) {
    super(message, id);
  }
}
