package org.hcmus.premiere.service;

import com.bastiaanjansen.otp.TOTP;
import org.hcmus.premiere.model.entity.OTP;

public interface OTPService {

  OTP sendOTPEmail(String toEmail);

  boolean verifyOTP(String otp, String email);

  TOTP generateTOTP();

  void sendOTPEmailRequestId(String toEmail, Long requestId);

  boolean verifyOTPRequestId(String otp, String email, Long requestId);
}
