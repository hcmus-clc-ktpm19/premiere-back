package org.hcmus.premiere.service;

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

  void externalTransfer(Transaction transaction);

  long getTotalPages(int size);

  String transferMoneyExternalBank(DepositMoneyExternalRequestDto depositMoneyExternalRequestDto)
  throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

  long getTotalPages(TransactionType transactionType, MoneyTransferCriteria moneyTransferCriteria,
      Long customerId, int size);

  long getTotalElements(TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria, Long customerId);

  @PreAuthorize("@SecurityUtils.isCustomer or @SecurityUtils.employeeOrAdmin")
  List<Transaction> getTransactionsByCustomerId(
      long page,
      long size,
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria, boolean isAsc,
      Long customerId);

  @PreAuthorize("hasRole('PREMIERE_ADMIN')")
  List<Transaction> getTransactionsByMonthAndInRangeOfDate(
      long page, long size, Long bankId,
      LocalDate fromDate, LocalDate toDate);
}
