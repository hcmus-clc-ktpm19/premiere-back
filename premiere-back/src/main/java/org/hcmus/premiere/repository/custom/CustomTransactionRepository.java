package org.hcmus.premiere.repository.custom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionType;

public interface CustomTransactionRepository {

  long count(TransactionType transactionType, MoneyTransferCriteria moneyTransferCriteria, Long customerId,
      LocalDate fromDate, LocalDate toDate);

  List<Transaction> getTransactionsByCustomerId(long page, long size, TransactionType transactionType,
      boolean isAsc, MoneyTransferCriteria moneyTransferCriteria, Long customerId,
      LocalDate fromDate, LocalDate toDate);

  List<Transaction> getTransactionsByBankIdAndInRangeOfDate(
      long page, long size,
      Long bankId, LocalDate fromDate, LocalDate toDate);

  BigDecimal getTotalAmountInRangeOfDate(LocalDate fromDate, LocalDate toDate, Long bankId);

  BigDecimal getTotalAmount();
}
