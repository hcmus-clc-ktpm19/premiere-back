package org.hcmus.premiere.model.exception;

public class OTPNotFoundException extends AbstractNotFoundException {

  public static final String OTP_NOT_FOUND_MESSAGE = "OTP not found";

  public OTPNotFoundException(String message, String id) {
    super(message, id);
  }

}