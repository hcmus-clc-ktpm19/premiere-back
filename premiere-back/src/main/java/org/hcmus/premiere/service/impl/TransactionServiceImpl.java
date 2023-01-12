package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V2_EXTERNAL;
import static org.hcmus.premiere.common.consts.PremiereApiUrls.TAIXIUBANK_API;

import com.google.gson.Gson;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.DepositMoneyExternalRequestDto;
import org.hcmus.premiere.model.dto.TransactionInfoExternalDto;
import org.hcmus.premiere.model.dto.TransferExternalHashDto;
import org.hcmus.premiere.model.dto.TransferExternalRequestDto;
import org.hcmus.premiere.model.dto.TransferExternalResponseDto;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    if(transaction.getStatus() == TransactionStatus.COMPLETED) {
      checkingTransaction.setStatus(TransactionStatus.COMPLETED);
      checkingTransactionService.updateCheckingTransactionStatus(checkingTransaction);
    } else {
      throw new IllegalArgumentException(Constants.MONEY_TRANSFER_TRANSACTION_FAILED_PLEASE_TRY_AGAIN);
    }
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
      transactionRepository.saveAndFlush(transaction);
    }
  }

  public void externalTransfer(Transaction transaction) {
    try {
      CreditCard senderCard = creditCardService.findCreditCardByNumber(transaction.getSenderCreditCardNumber());
      transaction.setSenderBalance(senderCard.getBalance());
      transaction.setReceiverBalance(BigDecimal.ZERO);

      TransactionInfoExternalDto transactionInfoExternalDto = new TransactionInfoExternalDto();
      transactionInfoExternalDto.setAccountDesNumber(transaction.getReceiverCreditCardNumber());
      transactionInfoExternalDto.setDescription(transaction.getTransactionRemark());
      transactionInfoExternalDto.setPayTransactionFee(Constants.PAY_TRANSACTION_FEE_EXTERNAL_SRC);

      if(transaction.isSelfPaymentFee()){
        senderCard.setBalance(senderCard.getBalance().subtract(transaction.getAmount().add(transaction.getFee())));
        transactionInfoExternalDto.setAmount(transaction.getAmount().longValue());
      } else {
        senderCard.setBalance(senderCard.getBalance().subtract(transaction.getAmount()));
        transactionInfoExternalDto.setAmount(transaction.getAmount().subtract(transaction.getFee()).longValue());
      }

      RestTemplate restTemplate = new RestTemplate();
      String servletPath = TAIXIUBANK_API + "/transactions/external/order-for-payment";
      String secretKey = securityUtils.getExternalSecretKey();
      long credentialsTime = System.currentTimeMillis();

      TransferExternalHashDto transferExternalHashDto = new TransferExternalHashDto();
      transferExternalHashDto.setAccountDesNumber(transactionInfoExternalDto.getAccountDesNumber());
      transferExternalHashDto.setAmount(transactionInfoExternalDto.getAmount());
      transferExternalHashDto.setDescription(transactionInfoExternalDto.getDescription());
      transferExternalHashDto.setPayTransactionFee(transactionInfoExternalDto.getPayTransactionFee());
      transferExternalHashDto.setAccountSrcNumber(transaction.getSenderCreditCardNumber());
      transferExternalHashDto.setSlug(Constants.PREMIERE_SLUG);

      Gson gson = new Gson();
      String data = gson.toJson(transferExternalHashDto);
      String msgToken = securityUtils.hashMd5(credentialsTime + data + secretKey);
      String rsaToken = securityUtils.encryptV2(data);

      TransferExternalRequestDto transferExternalRequestDto = new TransferExternalRequestDto();
      transferExternalRequestDto.setAccountNumber(transaction.getSenderCreditCardNumber());
      transferExternalRequestDto.setTransactionInfo(transactionInfoExternalDto);
      transferExternalRequestDto.setTimestamp(credentialsTime);
      transferExternalRequestDto.setMsgToken(msgToken);
      transferExternalRequestDto.setSlug(Constants.PREMIERE_SLUG);
      transferExternalRequestDto.setSignature(rsaToken);

      creditCardService.updateCreditCard(senderCard);
      HttpEntity<?> request = new HttpEntity<>(transferExternalRequestDto, null);
      ResponseEntity<TransferExternalResponseDto> response = restTemplate.exchange(servletPath, HttpMethod.POST, request, TransferExternalResponseDto.class);
      if(securityUtils.verify(gson.toJson(Objects.requireNonNull(response.getBody()).getData()), Objects.requireNonNull(response.getBody()).getSignature(), transaction.getReceiverBank().getPublicKey())) {
        transaction.setStatus(TransactionStatus.COMPLETED);
      } else {
        transaction.setStatus(TransactionStatus.FAILED);
      }
    } catch (Exception e) {
      transaction.setStatus(TransactionStatus.FAILED);
    } finally {
      transactionRepository.saveAndFlush(transaction);
    }
  }

  public void externalTransferTest(Transaction transaction) {
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
      int size) {
    long count = getTotalElements(transactionType, moneyTransferCriteria, customerId);
    long totalPages = count / size;
    return totalPages + (count % size == 0 ? 0 : 1);
  }

  @Override
  public long getTotalElements(TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria, Long customerId) {
    return transactionRepository.count(transactionType, moneyTransferCriteria, customerId);
  }

  @Override
  public List<Transaction> getTransactionsByCustomerId(
      long page,
      long size,
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria,
      boolean isAsc,
      Long customerId) {
    return transactionRepository.getTransactionsByCustomerId(
        page,
        size,
        transactionType,
        isAsc,
        moneyTransferCriteria,
        customerId);
  }
}
