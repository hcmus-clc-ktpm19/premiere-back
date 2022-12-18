package org.hcmus.premiere.service;

import com.bastiaanjansen.otp.TOTP;

public interface OTPService {
  void sendOTPEmail(String toEmail);
  Boolean verifyOTP(String otp, String email);
  TOTP generateTOTP();
}
