package org.hcmus.premiere.service.impl;

import java.util.List;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.service.EmailService;
import org.springframework.stereotype.Service;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

  private final ApiKeyAuth apiKeyAuth;
  @Override
  public void sendOTPEmail(String toEmail) {
    // send email
    TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
    SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
    Properties headers = new Properties();
    headers.setProperty("Some-Custom-Name", "unique-id-1234");
    Properties params = new Properties();
    params.setProperty("parameter", "My param value");
    params.setProperty("subject", "New Subject");
    sendSmtpEmail.setSubject("Test send email");
    sendSmtpEmail.setHtmlContent("Test send email");
    sendSmtpEmail.setSender(new sibModel.SendSmtpEmailSender().name("PREMIERE").email("premiere-noreply@proton.me"));
    sendSmtpEmail.setTo(List.of(new sibModel.SendSmtpEmailTo().email(toEmail)));
    sendSmtpEmail.setReplyTo(new sibModel.SendSmtpEmailReplyTo().email("premiere-noreply@proton.me"));
    sendSmtpEmail.setHeaders(headers);
    sendSmtpEmail.setParams(params);
    try {
     apiInstance.sendTransacEmail(sendSmtpEmail);
    } catch (Exception e) {
      System.err.println("Exception when calling TransactionalEmailsApi#sendTransacEmail");
      e.printStackTrace();
    }
  }
}
