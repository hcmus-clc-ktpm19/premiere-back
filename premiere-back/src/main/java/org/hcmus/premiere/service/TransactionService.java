package org.hcmus.premiere.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.hcmus.premiere.model.dto.DepositMoneyExternalRequestDto;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;

public interface TransactionService {

  long count();

  void transfer(TransferMoneyRequestDto transferMoneyRequestDto);

  void internalTransfer(Transaction transaction);

  void externalTransferTest(Transaction transaction);

  void externalTransfer(Transaction transaction);

  long getTotalPages(int size);

  String transferMoneyExternalBank(DepositMoneyExternalRequestDto depositMoneyExternalRequestDto)
  throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

  long getTotalPages(TransactionType transactionType, MoneyTransferCriteria moneyTransferCriteria,
      Long customerId, int size, LocalDate fromDate, LocalDate toDate);

  long getTotalElements(TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria, Long customerId, LocalDate fromDate,
      LocalDate toDate);

  @PreAuthorize("@SecurityUtils.isCustomer or @SecurityUtils.employeeOrAdmin")
  List<Transaction> getTransactionsByCustomerId(
      long page,
      long size,
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria, boolean isAsc,
      Long customerId, LocalDate fromDate, LocalDate toDate);

  @PreAuthorize("hasRole('PREMIERE_ADMIN')")
  List<Transaction> getTransactionsByBankIdAndInRangeOfDate(
      long page, long size, Long bankId,
      LocalDate fromDate, LocalDate toDate);

  List<BigDecimal> getTotalAmountInRangeOfDate(LocalDate fromDate, LocalDate toDate, Long bankId);

  BigDecimal getTotalAmount();
}
