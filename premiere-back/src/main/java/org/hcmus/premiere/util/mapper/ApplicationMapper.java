package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.dto.OTPDto;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.model.entity.OTP;
import org.hcmus.premiere.model.entity.Receiver;
import org.springframework.stereotype.Component;


@Component
public class ApplicationMapper {

  public CreditCardDto toCreditCardDto(CreditCard creditCard) {
    if (creditCard == null) {
      return null;
    } else {
      CreditCardDto creditCardDto = new CreditCardDto();
      creditCardDto.setCardNumber(creditCard.getCardNumber());
      creditCardDto.setBalance(creditCard.getBalance());
      return creditCardDto;
    }
  }

  public ReceiverDto toReceiverDto(Receiver receiver) {
    if (receiver == null) {
      return null;
    } else {
      ReceiverDto receiverDto = new ReceiverDto();
      receiverDto.setId(receiver.getId());
      receiverDto.setCardNumber(receiver.getCardNumber());
      receiverDto.setNickname(receiver.getNickname());
      receiverDto.setFullName(receiver.getFullName());
      receiverDto.setUserId(receiver.getUser().getId());
      receiverDto.setBankName(receiver.getBank().getBankName());
      return receiverDto;
    }
  }

  public OTPDto toOTPDto(OTP otp){
    if (otp == null) {
      return null;
    } else {
      OTPDto otpDto = new OTPDto();
      otpDto.setOtp(otp.getOtp());
      otpDto.setEmail(otp.getEmail());
      otpDto.setCreatedAt(otp.getCreatedAt());
      return otpDto;
    }
  }

  public LoanReminderDto toLoanReminderDto(LoanReminder loanReminder){
    if (loanReminder == null) {
      return null;
    } else {
      LoanReminderDto loanReminderDto = new LoanReminderDto();
      loanReminderDto.setId(loanReminder.getId());
      loanReminderDto.setVersion(loanReminder.getVersion());
      loanReminderDto.setTransferAmount(loanReminder.getLoanBalance());
      loanReminderDto.setStatus(loanReminder.getStatus());
      loanReminderDto.setTime(loanReminder.getTime());
      loanReminderDto.setLoanRemark(loanReminder.getLoanRemark());
      loanReminderDto.setSenderCreditCardId(loanReminder.getSenderCreditCard().getId());
      loanReminderDto.setReceiverCreditCardId(loanReminder.getReceiverCreditCard().getId());
      return loanReminderDto;
    }
  }
}
