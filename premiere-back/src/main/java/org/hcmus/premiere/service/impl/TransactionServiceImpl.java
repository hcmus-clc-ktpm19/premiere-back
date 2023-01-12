package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V2_EXTERNAL;

import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.DepositMoneyExternalRequestDto;
import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;
import org.hcmus.premiere.model.entity.CheckingTransaction;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.repository.TransactionRepository;
import org.hcmus.premiere.resource.ExternalBankResource;
import org.hcmus.premiere.service.CheckingTransactionService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.OTPService;
import org.hcmus.premiere.service.TransactionService;
import org.hcmus.premiere.util.security.SecurityUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  private final CreditCardService creditCardService;

  private final OTPService otpService;

  private final CheckingTransactionService checkingTransactionService;

  private final SecurityUtils securityUtils;

  private final ExternalBankResource resource;

  private final Environment env;


  public TransactionServiceImpl(
      TransactionRepository transactionRepository,
      CreditCardService creditCardService,
      OTPService otpService,
      CheckingTransactionService checkingTransactionService,
      SecurityUtils securityUtils,
      ResteasyWebTarget resteasyWebTarget,
      Environment env) {
    this.transactionRepository = transactionRepository;
    this.creditCardService = creditCardService;
    this.otpService = otpService;
    this.checkingTransactionService = checkingTransactionService;
    this.securityUtils = securityUtils;
    this.resource = resteasyWebTarget.proxy(ExternalBankResource.class);
    this.env = env;
  }


  @Override
  public long count() {
    return transactionRepository.count();
  }

  @Override
  public void transfer(TransferMoneyRequestDto transferMoneyRequestDto) {
    CheckingTransaction checkingTransaction = checkingTransactionService.getCheckingTransactionById(transferMoneyRequestDto.getRequestID());

    if(checkingTransaction.getStatus() == TransactionStatus.COMPLETED) {
      throw new IllegalArgumentException(Constants.TRANSACTION_ALREADY_COMPLETED);
    }

    if(!verifyOTP(transferMoneyRequestDto.getOtp(), checkingTransaction)) {
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
    transaction.setStatus(checkingTransaction.getStatus());

    if (checkingTransaction.isInternal()) {
      internalTransfer(transaction);
    } else {
      externalTransfer(transaction);
    }
    checkingTransactionService.updateCheckingTransactionStatus(checkingTransaction);
  }

  public void internalTransfer(Transaction transaction) {
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
      transaction.setStatus(TransactionStatus.COMPLETED);
    } catch (Exception e) {
      transaction.setStatus(TransactionStatus.FAILED);
    } finally {
      transactionRepository.save(transaction);
    }
  }

  public void externalTransfer(Transaction transaction) {
    try {
      CreditCard senderCard = creditCardService.findCreditCardByNumber(transaction.getSenderCreditCardNumber());
      transaction.setSenderBalance(senderCard.getBalance());
      transaction.setReceiverBalance(BigDecimal.ZERO);
      DepositMoneyExternalRequestDto depositMoneyExternalRequestDto = new DepositMoneyExternalRequestDto();
      depositMoneyExternalRequestDto.setSenderCreditCardNumber(transaction.getSenderCreditCardNumber());
      depositMoneyExternalRequestDto.setReceiverCreditCardNumber(transaction.getReceiverCreditCardNumber());
      depositMoneyExternalRequestDto.setSenderBankName(transaction.getSenderBank().getBankName());
      if(transaction.isSelfPaymentFee()){
        senderCard.setBalance(senderCard.getBalance().subtract(transaction.getAmount().add(transaction.getFee())));
        depositMoneyExternalRequestDto.setAmount(transaction.getAmount());
      } else {
        senderCard.setBalance(senderCard.getBalance().subtract(transaction.getAmount()));
        depositMoneyExternalRequestDto.setAmount(transaction.getAmount().subtract(transaction.getFee()));
      }
      creditCardService.updateCreditCard(senderCard);
      depositMoneyExternalBankAccountRequest(depositMoneyExternalRequestDto);
      transaction.setStatus(TransactionStatus.COMPLETED);
    } catch (Exception e) {
      transaction.setStatus(TransactionStatus.FAILED);
    } finally {
      transactionRepository.save(transaction);
    }
  }

  private void depositMoneyExternalBankAccountRequest(DepositMoneyExternalRequestDto depositMoneyExternalRequestDto)
      throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    String servletPath = PREMIERE_API_V2_EXTERNAL + "/banks/transactions/money-transfer";
    String credentialsTime = LocalDateTime.now(ZoneId.systemDefault()).toString();
    String zoneId = ZoneId.systemDefault().toString();
    String secretKey = securityUtils.getSecretKey();
    String authToken = securityUtils.hash(servletPath + credentialsTime + zoneId + secretKey);
    String rsaToken = securityUtils.encrypt(env.getProperty("system-auth.secret-key"), true);
    depositMoneyExternalRequestDto.setRsaToken(rsaToken);
    try{
      Map<String, String> res = (Map<String, String>) resource.transferMoneyExternalBankId(
          authToken,
          credentialsTime,
          zoneId,
          depositMoneyExternalRequestDto
      );
      if(!securityUtils.decrypt(res.get("rsaToken"), true).equals(env.getProperty("system-auth.secret-key"))) {
        throw new IllegalArgumentException(Constants.INVALID_RSA_TOKEN);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(Constants.EXTERNAL_BANK_ERROR);
    }
  }

  @Override
  public String transferMoneyExternalBank(DepositMoneyExternalRequestDto depositMoneyExternalRequestDto)
      throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    if(!securityUtils.decrypt(depositMoneyExternalRequestDto.getRsaToken(), true).equals(env.getProperty("system-auth.secret-key"))) {
      throw new IllegalArgumentException(Constants.INVALID_RSA_TOKEN);
    }
    CreditCard receiverCard = creditCardService.findCreditCardByNumberExternal(depositMoneyExternalRequestDto.getReceiverCreditCardNumber());
    receiverCard.setBalance(receiverCard.getBalance().add(depositMoneyExternalRequestDto.getAmount()));
    creditCardService.updateCreditCard(receiverCard);
    //TODO: save transaction external bank
    return securityUtils.encrypt(env.getProperty("system-auth.secret-key"), true);
  }

  private boolean verifyOTP(String otp, CheckingTransaction checkingTransaction){
    CreditCard senderCard = creditCardService.findCreditCardByNumber(checkingTransaction.getSenderCreditCardNumber());
    return otpService.verifyOTPRequestId(otp, senderCard.getUser().getEmail(), checkingTransaction.getId());
  }

  @Override
  public long getTotalPages(
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria,
      Long customerId,
      int size,
      LocalDate fromDate,
      LocalDate toDate) {
    long count = getTotalElements(transactionType, moneyTransferCriteria, customerId, fromDate, toDate);
    long totalPages = count / size;
    return totalPages + (count % size == 0 ? 0 : 1);
  }


  @Override
  public long getTotalPages(int size) {
    long count = count();
    return count + (count % size == 0 ? 0 : 1);
  }

  @Override
  public long getTotalElements(
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria,
      Long customerId,
      LocalDate fromDate,
      LocalDate toDate) {
    return transactionRepository.count(transactionType, moneyTransferCriteria, customerId, fromDate, toDate);
  }

  @Override
  public List<Transaction> getTransactionsByCustomerId(
      long page,
      long size,
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria,
      boolean isAsc,
      Long customerId,
      LocalDate fromDate,
      LocalDate toDate) {
    return transactionRepository.getTransactionsByCustomerId(
        page,
        size,
        transactionType,
        isAsc,
        moneyTransferCriteria,
        customerId,
        fromDate,
        toDate);
  }

  @Override
  public List<Transaction> getTransactionsByBankIdAndInRangeOfDate(
      long page,
      long size,
      Long bankId,
      LocalDate fromDate,
      LocalDate toDate) {
    if (fromDate.isAfter(toDate)) {
      throw new IllegalArgumentException();
    }

    if (fromDate.datesUntil(toDate).count() > 31) {
      throw new IllegalArgumentException();
    }

    return transactionRepository.getTransactionsByBankIdAndInRangeOfDate(
        page,
        size,
        bankId,
        fromDate,
        toDate);
  }
}
