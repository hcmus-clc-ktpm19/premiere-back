package org.hcmus.premiere.model.exception;

public class OTPNotFoundException extends AbstractNotFoundException {

  public static final String OTP_NOT_FOUND_MESSAGE = "OTP not found";

  public static final String OTP_NOT_FOUND = "OTP.NOT_FOUND";

  public OTPNotFoundException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }

}