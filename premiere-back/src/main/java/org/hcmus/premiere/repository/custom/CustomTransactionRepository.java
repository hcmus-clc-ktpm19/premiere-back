package org.hcmus.premiere.repository.custom;

import java.time.LocalDate;
import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;

public interface CustomTransactionRepository {

  long count(TransactionType transactionType, Long customerId);

  List<Transaction> getTransactionsByCustomerId(long page, long size,
      TransactionType transactionType,
      boolean isAsc, Long customerId);

  List<Transaction> getTransactionsByMonthAndInRangeOfDate(
      long page, long size,
      Long bankId, LocalDate fromDate, LocalDate toDate);
}
