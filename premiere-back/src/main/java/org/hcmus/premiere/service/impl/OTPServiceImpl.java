package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.OTPNotFoundException.OTP_NOT_FOUND;
import static org.hcmus.premiere.model.exception.OTPNotFoundException.OTP_NOT_FOUND_MESSAGE;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTP;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.entity.OTP;
import org.hcmus.premiere.model.exception.OTPNotFoundException;
import org.hcmus.premiere.repository.OTPRepository;
import org.hcmus.premiere.service.OTPService;
import org.springframework.stereotype.Service;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

  private final ApiKeyAuth apiKeyAuth;
  private final OTPRepository otpRepository;

  @Override
  public TOTP generateTOTP() {
    byte[] secret = SecretGenerator.generate();

    TOTP.Builder builder = new TOTP.Builder(secret);

    builder
        .withPasswordLength(6)
        .withAlgorithm(HMACAlgorithm.SHA1) // SHA256 and SHA512 are also supported
        .withPeriod(Duration.ofMinutes(5)); // valid for 5 minutes

    return builder.build();
  }

  @Override
  public OTP sendOTPEmail(String toEmail) {
    // generate time-based OTP
    TOTP totp = generateTOTP();
    String otp = totp.now();

    // send email
    TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
    SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
    Properties headers = new Properties();
    headers.setProperty("Some-Custom-Name", "unique-id-1234");
    Properties params = new Properties();
    params.setProperty("parameter", "My param value");
    params.setProperty("subject", "New Subject");
    sendSmtpEmail.setSubject("OTP for reset password");
    sendSmtpEmail.setHtmlContent("Hello, your OTP is " + otp + " and it will be expired in 5 minutes");
    sendSmtpEmail.setSender(new sibModel.SendSmtpEmailSender().name("PREMIERE").email("premiere-noreply@proton.me"));
    sendSmtpEmail.setTo(List.of(new sibModel.SendSmtpEmailTo().email(toEmail)));
    sendSmtpEmail.setReplyTo(new sibModel.SendSmtpEmailReplyTo().email("premiere-noreply@proton.me"));
    sendSmtpEmail.setHeaders(headers);
    sendSmtpEmail.setParams(params);
    try {
     apiInstance.sendTransacEmail(sendSmtpEmail);
     // save otp to db
      OTP otpEntity = new OTP();
      otpEntity.setOtp(otp);
      otpEntity.setEmail(toEmail);
      return otpRepository.saveAndFlush(otpEntity);
    } catch (Exception e) {
      log.error("Exception when calling TransactionalEmailsApi#sendTransacEmail");
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean verifyOTP(String otp, String email) {
    // find the latest otp by email
    OTP otpResult = otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
        .orElseThrow(
            () -> new OTPNotFoundException(OTP_NOT_FOUND_MESSAGE + " for ", email, OTP_NOT_FOUND));
    LocalDateTime expiredTime = otpResult.getCreatedAt().plusMinutes(5);
    if (LocalDateTime.now().isAfter(expiredTime)) {
      return false;
    }
    // if OTP is verified, delete it
    if (otpResult.getOtp().equals(otp)) {
      otpRepository.delete(otpResult);
      return true;
    }
    return false;
  }
  @Override
  public void sendOTPEmailRequestId(String toEmail, Long requestId) {
    OTP otpEntity = sendOTPEmail(toEmail);
    otpEntity.setRequestId(requestId);
    otpRepository.saveAndFlush(otpEntity);
  }

  @Override
  public boolean verifyOTPRequestId(String otp, String email, Long requestId) {
    OTP otpResult = otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
        .orElseThrow(
            () -> new OTPNotFoundException(OTP_NOT_FOUND_MESSAGE + " for ", email, OTP_NOT_FOUND));
    LocalDateTime expiredTime = otpResult.getCreatedAt().plusMinutes(5);
    if (LocalDateTime.now().isAfter(expiredTime)) {
      return false;
    }
    if (otpResult.getOtp().equals(otp) && otpResult.getRequestId().equals(requestId)) {
      otpRepository.delete(otpResult);
      return true;
    }
    return false;
  }
}
