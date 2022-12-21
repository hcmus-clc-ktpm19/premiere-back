package org.hcmus.premiere.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.hcmus.premiere.common.Constants;
import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.model.exception.BankNotFoundException;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
import org.hcmus.premiere.repository.TransactionRepository;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.OTPService;
import org.hcmus.premiere.service.TransactionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@Transactional(rollbackFor = {CreditCardNotFoundException.class, BankNotFoundException.class ,IllegalArgumentException.class, DataIntegrityViolationException.class, MethodArgumentNotValidException.class})
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  private final CreditCardService creditCardService;

  private final BankService bankService;

  private final OTPService otpService;

//  @Override
//  public Transaction createTransaction(TransactionRequestDto transactionRequestDto){
//    Transaction transaction = new Transaction();
//    transaction.setSenderCreditCardNumber(transactionRequestDto.getSenderCardNumber());
//    transaction.setAmount(amount);
//    transaction.setType(TransactionType.valueOf(transactionRequestDto.getType()));
//    transaction.setSelfPaymentFee(transactionRequestDto.getIsSelfPaymentFee());
//    transaction.setFee(amount.multiply(Constants.TRANSACTION_FEE));
//    transaction.setTransactionRemark(transactionRequestDto.getRemark());
//    transaction.setSenderBank(premiereBank);
//    transaction.setTime(LocalDateTime.now());
//    transaction.setStatus(TransactionStatus.CHECKING);
//  }

  @Override
  public void transfer(TransactionRequestDto transactionRequestDto) {
    if(!verifyOTP(transactionRequestDto.getOtp(),transactionRequestDto.getSenderCardNumber())) {
      throw new IllegalArgumentException(Constants.OTP_IS_NOT_VALID);
    }

    BigDecimal amount = new BigDecimal(transactionRequestDto.getAmount());
    Bank premiereBank = bankService.findBankByName(Constants.PREMIERE_BANK_NAME);

    Transaction transaction = new Transaction();
    transaction.setSenderCreditCardNumber(transactionRequestDto.getSenderCardNumber());
    transaction.setAmount(amount);
    transaction.setType(TransactionType.valueOf(transactionRequestDto.getType()));
    transaction.setSelfPaymentFee(transactionRequestDto.getIsSelfPaymentFee());
    transaction.setFee(amount.multiply(Constants.TRANSACTION_FEE));
    transaction.setTransactionRemark(transactionRequestDto.getRemark());
    transaction.setSenderBank(premiereBank);
    transaction.setTime(LocalDateTime.now());
    transaction.setStatus(TransactionStatus.CHECKING);

    if (transactionRequestDto.getIsInternal()) {
      transaction.setReceiverCreditCardNumber(transactionRequestDto.getReceiverCardNumber());
      transaction.setReceiverBank(premiereBank);
      internalTransfer(transaction);
    } else {
      Bank receiverBank = bankService.findBankByName(transactionRequestDto.getReceiverBankName());
      transaction.setReceiverBank(receiverBank);
      externalTransfer(transaction);
    }
  }

  private void internalTransfer(Transaction transaction) {
    try {
      CreditCard senderCard = creditCardService.findCreditCardByNumber(transaction.getSenderCreditCardNumber());
      CreditCard receiverCard = creditCardService.findCreditCardByNumber(transaction.getReceiverCreditCardNumber());
      transaction.setSenderBalance(senderCard.getBalance());
      transaction.setReceiverBalance(receiverCard.getBalance());
      if(transaction.isSelfPaymentFee()){
        senderCard.setBalance(senderCard.getBalance().subtract(transaction.getAmount().add(transaction.getFee())));
        receiverCard.setBalance(receiverCard.getBalance().add(transaction.getAmount()));
      } else {
        senderCard.setBalance(senderCard.getBalance().subtract(transaction.getAmount()));
        receiverCard.setBalance(receiverCard.getBalance().add(transaction.getAmount().subtract(transaction.getFee())));
      }
      creditCardService.updateCreditCard(senderCard);
      creditCardService.updateCreditCard(receiverCard);
      transaction.setTime(LocalDateTime.now());
      transaction.setStatus(TransactionStatus.COMPLETED);
    } catch (Exception e) {
      transaction.setTime(LocalDateTime.now());
      transaction.setStatus(TransactionStatus.FAILED);
    } finally {
      transactionRepository.save(transaction);
    }
  }

  private void externalTransfer(Transaction transaction) {
    // TODO: External transfer
  }



  @Override
  public void sendOTP(String senderCardNumber){
    CreditCard senderCard = creditCardService.findCreditCardByNumber(senderCardNumber);
    otpService.sendOTPEmail(senderCard.getUser().getEmail());
  }

  private boolean verifyOTP(String otp, String senderCardNumber){
    CreditCard senderCard = creditCardService.findCreditCardByNumber(senderCardNumber);
    return otpService.verifyOTP(otp, senderCard.getUser().getEmail());
  }


}
