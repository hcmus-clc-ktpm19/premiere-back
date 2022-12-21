package org.hcmus.premiere.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

  @Override
  public void transfer(TransactionRequestDto transactionRequestDto) {
    BigDecimal amount = new BigDecimal(transactionRequestDto.getAmount());

    if (amount.compareTo(Constants.MINIMUM_AMOUNT_TO_WITHDRAW) < 0) {
      throw new IllegalArgumentException(Constants.THE_TRANSFER_AMOUNT_IS_NOT_VALID);
    }

    BigDecimal fee = amount.multiply(Constants.TRANSACTION_FEE);
    BigDecimal totalAmount, amountReceived;
    if(transactionRequestDto.getIsSelfPaymentFee()) {
      totalAmount = amount.add(fee);
      amountReceived = amount;
    } else {
      totalAmount = amount;
      amountReceived = amount.subtract(fee);
    }

    CreditCard senderCard = creditCardService.findCreditCardByNumber(transactionRequestDto.getSenderCardNumber());
    if (senderCard.getBalance().subtract(totalAmount).compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(Constants.ACCOUNT_CURRENT_BALANCE_IS_NOT_AVAILABLE_TO_WITHDRAW);
    }

    Bank premiereBank = bankService.findBankByName(Constants.PREMIERE_BANK_NAME);

    Transaction transaction = new Transaction();
    transaction.setSenderCreditCardNumber(senderCard.getCardNumber());
    transaction.setAmount(amount);
    transaction.setType(TransactionType.valueOf(transactionRequestDto.getType()));
    transaction.setSelfPaymentFee(transactionRequestDto.getIsSelfPaymentFee());
    transaction.setFee(fee);
    transaction.setTransactionRemark(transactionRequestDto.getRemark());
    transaction.setSenderBank(premiereBank);
    transaction.setTime(LocalDateTime.now());
    transaction.setStatus(TransactionStatus.CHECKING);

    if (transactionRequestDto.getIsInternal()) {
      transaction.setReceiverBank(premiereBank);
      internalTransfer(transaction, transactionRequestDto, senderCard, totalAmount, amountReceived);
    } else {
      if(StringUtils.isEmpty(transactionRequestDto.getReceiverBankName())) {
        throw new IllegalArgumentException(Constants.RECEIVER_BANK_NAME_IS_NOT_VALID);
      }
      Bank receiverBank = bankService.findBankByName(transactionRequestDto.getReceiverBankName());
      transaction.setReceiverBank(receiverBank);
      externalTransfer(transaction, transactionRequestDto, senderCard, totalAmount, amountReceived);
    }
  }

  private void internalTransfer(Transaction transaction, TransactionRequestDto transactionRequestDto, CreditCard senderCard, BigDecimal totalAmount, BigDecimal amountReceived) {
    CreditCard receiverCard = creditCardService.findCreditCardByNumber(transactionRequestDto.getReceiverCardNumber());
    try {
      transaction.setReceiverCreditCardNumber(receiverCard.getCardNumber());
      transaction.setSenderBalance(senderCard.getBalance());
      transaction.setReceiverBalance(receiverCard.getBalance());
      senderCard.setBalance(senderCard.getBalance().subtract(totalAmount));
      receiverCard.setBalance(receiverCard.getBalance().add(amountReceived));
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

  private void externalTransfer(Transaction transaction, TransactionRequestDto transactionRequestDto, CreditCard senderCard, BigDecimal totalAmount, BigDecimal amountReceived) {
    // TODO: External transfer
  }


}
