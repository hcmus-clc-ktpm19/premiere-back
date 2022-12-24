package org.hcmus.premiere.service.impl;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.hcmus.premiere.common.Constants;
import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;
import org.hcmus.premiere.model.entity.CheckingTransaction;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.repository.TransactionRepository;
import org.hcmus.premiere.service.CheckingTransactionService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.OTPService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.repository.TransactionRepository;
import org.hcmus.premiere.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  private final CreditCardService creditCardService;

  private final OTPService otpService;

  private final CheckingTransactionService checkingTransactionService;

  @Override
  public void transfer(TransferMoneyRequestDto transferMoneyRequestDto) {
    CheckingTransaction checkingTransaction = checkingTransactionService.getCheckingTransactionById(transferMoneyRequestDto.getCheckingTransactionId());

    if(!verifyOTP(transferMoneyRequestDto.getOtp(), checkingTransaction.getSenderCreditCardNumber())) {
      throw new IllegalArgumentException(Constants.OTP_IS_NOT_VALID);
    }

    Transaction transaction = new Transaction();
    transaction.setSenderCreditCardNumber(checkingTransaction.getSenderCreditCardNumber());
    transaction.setReceiverCreditCardNumber(checkingTransaction.getReceiverCreditCardNumber());
    transaction.setAmount(checkingTransaction.getAmount());
    transaction.setType(checkingTransaction.getType());
    transaction.setSelfPaymentFee(checkingTransaction.isSelfPaymentFee());
    transaction.setFee(checkingTransaction.getFee());
    transaction.setTransactionRemark(checkingTransaction.getTransactionRemark());
    transaction.setSenderBank(checkingTransaction.getSenderBank());
    transaction.setReceiverBank(checkingTransaction.getReceiverBank());
    transaction.setTime(checkingTransaction.getTime());
    transaction.setStatus(checkingTransaction.getStatus());

    if (checkingTransaction.isInternal()) {
      internalTransfer(transaction);
    } else {
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

  private boolean verifyOTP(String otp, String senderCardNumber){
    CreditCard senderCard = creditCardService.findCreditCardByNumber(senderCardNumber);
    return otpService.verifyOTP(otp, senderCard.getUser().getEmail());
  }


  private final TransactionRepository transactionRepository;

  @Override
  public long getTotalPages(TransactionType transactionType, Long customerId, int size) {
    long count = getTotalElements(transactionType, customerId);
    long totalPages = count / size;
    return totalPages + (count % size == 0 ? 0 : 1);
  }

  @Override
  public long getTotalElements(TransactionType transactionType, Long customerId) {
    return transactionRepository.count(transactionType, customerId);
  }

  @Override
  public List<Transaction> getTransactionsByCustomerId(long page, long size, TransactionType transactionType,
      boolean isAsc, Long customerId) {
    return transactionRepository.getTransactionsByCustomerId(
        page,
        size,
        transactionType,
        isAsc,
        customerId);
  }
}
