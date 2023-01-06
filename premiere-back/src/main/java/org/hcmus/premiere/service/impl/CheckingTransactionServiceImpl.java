package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.CheckingTransactionNotFoundException.CHECKING_TRANSACTION_NOT_FOUND;
import static org.hcmus.premiere.model.exception.CheckingTransactionNotFoundException.CHECKING_TRANSACTION_NOT_FOUND_MESSAGE;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.CheckingTransaction;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.model.exception.CheckingTransactionNotFoundException;
import org.hcmus.premiere.repository.CheckingTransactionRepository;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CheckingTransactionService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.OTPService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@AllArgsConstructor
public class CheckingTransactionServiceImpl implements CheckingTransactionService {

  private final CheckingTransactionRepository checkingTransactionRepository;

  private final CreditCardService creditCardService;

  private final BankService bankService;

  private final OTPService otpService;

  @Override
  public CheckingTransaction getCheckingTransactionById(Long id) {
    return checkingTransactionRepository.findById(id)
        .orElseThrow(() -> new CheckingTransactionNotFoundException(CHECKING_TRANSACTION_NOT_FOUND_MESSAGE, id.toString(), CHECKING_TRANSACTION_NOT_FOUND));
  }

  @Override
  public Long sendOTP(TransactionRequestDto transactionRequestDto) {
    Long requestId = createCheckingTransaction(transactionRequestDto);
    CreditCard senderCard = creditCardService.findCreditCardByNumber(transactionRequestDto.getSenderCardNumber());
    otpService.sendOTPEmailRequestId(senderCard.getUser().getEmail(), requestId);
    return requestId;
  }

  private Long createCheckingTransaction(TransactionRequestDto transactionRequestDto) {
    BigDecimal amount = new BigDecimal(transactionRequestDto.getAmount());
    Bank premiereBank = bankService.findBankByName(Constants.PREMIERE_BANK_NAME);
    CheckingTransaction checkingTransaction = new CheckingTransaction();

    checkingTransaction.setSenderCreditCardNumber(transactionRequestDto.getSenderCardNumber());
    checkingTransaction.setReceiverCreditCardNumber(transactionRequestDto.getReceiverCardNumber());
    checkingTransaction.setAmount(amount);
    checkingTransaction.setType(TransactionType.valueOf(transactionRequestDto.getType()));
    checkingTransaction.setSelfPaymentFee(transactionRequestDto.getIsSelfPaymentFee());
    checkingTransaction.setFee(amount.multiply(Constants.TRANSACTION_FEE));
    checkingTransaction.setTransactionRemark(transactionRequestDto.getRemark());
    checkingTransaction.setSenderBank(premiereBank);
    checkingTransaction.setInternal(transactionRequestDto.getIsInternal());

    if (checkingTransaction.isInternal()) {
      checkingTransaction.setReceiverBank(premiereBank);
    } else {
      checkingTransaction.setReceiverBank(bankService.findBankByName(transactionRequestDto.getReceiverBankName()));
    }

    checkingTransaction.setStatus(TransactionStatus.CHECKING);
    checkingTransactionRepository.saveAndFlush(checkingTransaction);
    return checkingTransaction.getId();
  }

  @Override
  public void updateCheckingTransactionStatus(CheckingTransaction checkingTransaction) {
    checkingTransaction.setStatus(TransactionStatus.COMPLETED);
    checkingTransactionRepository.saveAndFlush(checkingTransaction);
  }


}
