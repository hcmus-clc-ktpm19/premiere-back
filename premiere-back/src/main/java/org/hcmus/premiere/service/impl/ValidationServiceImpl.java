package org.hcmus.premiere.service.impl;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.model.enums.LoanStatus;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Throwable.class)
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {

  private final CreditCardService creditCardService;

  private final BankService bankService;

  @Override
  public Boolean validateTransactionRequest(TransactionRequestDto transactionRequestDto) {
    if(StringUtils.equals(transactionRequestDto.getSenderCardNumber(), transactionRequestDto.getReceiverCardNumber())) {
      throw new IllegalArgumentException(Constants.SENDER_AND_RECEIVER_ARE_SAME);
    }

    BigDecimal amount = new BigDecimal(transactionRequestDto.getAmount());
    if (amount.compareTo(Constants.MINIMUM_AMOUNT_TO_WITHDRAW) < 0) {
      throw new IllegalArgumentException(Constants.THE_TRANSFER_AMOUNT_IS_NOT_VALID);
    }
    BigDecimal totalAmount;
    if (transactionRequestDto.getIsSelfPaymentFee()) {
      totalAmount = amount.add(amount.multiply(Constants.TRANSACTION_FEE));
    } else {
      totalAmount = amount;
    }
    CreditCard senderCard = creditCardService.findCreditCardByNumber(transactionRequestDto.getSenderCardNumber());
    if (senderCard.getBalance().subtract(totalAmount).compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(Constants.ACCOUNT_CURRENT_BALANCE_IS_NOT_AVAILABLE_TO_WITHDRAW);
    }

    if (transactionRequestDto.getIsInternal()) {
      creditCardService.findCreditCardByNumber(transactionRequestDto.getReceiverCardNumber());
    } else {
      if(StringUtils.isEmpty(transactionRequestDto.getReceiverBankName())) {
        throw new IllegalArgumentException(Constants.BANK_NAME_IS_NOT_VALID);
      }
      Bank receiverBank = bankService.findBankByName(transactionRequestDto.getReceiverBankName());
      creditCardService.getCreditCardByNumberAndExternalBankId(receiverBank.getId(), transactionRequestDto.getReceiverCardNumber());
    }
    return true;
  }

  @Override
  public Boolean validateLoanReminderRequest(LoanReminder loanReminder) {
    if(loanReminder.getStatus() != LoanStatus.PENDING) {
      throw new IllegalArgumentException(Constants.LOAN_REMINDER_STATUS_IS_NOT_PENDING);
    }

    if (loanReminder.getSenderCreditCard() == null || loanReminder.getReceiverCreditCard() == null
        || loanReminder.getSenderCreditCard() == loanReminder.getReceiverCreditCard()) {
      throw new IllegalArgumentException(Constants.SENDER_AND_RECEIVER_ARE_SAME);
    }

    if (loanReminder.getLoanBalance().compareTo(Constants.MINIMUM_AMOUNT_TO_WITHDRAW) < 0) {
      throw new IllegalArgumentException(Constants.THE_TRANSFER_AMOUNT_IS_NOT_VALID);
    }

    if (loanReminder.getReceiverCreditCard().getBalance().subtract(loanReminder.getLoanBalance()
            .add(loanReminder.getLoanBalance().multiply(Constants.TRANSACTION_FEE)))
        .compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(
          Constants.ACCOUNT_CURRENT_BALANCE_IS_NOT_AVAILABLE_TO_WITHDRAW);
    }
    return true;
  }


}
