package org.hcmus.premiere.repository.custom;

import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionType;

public interface CustomTransactionRepository {

  long count(TransactionType transactionType, MoneyTransferCriteria moneyTransferCriteria, Long customerId);

  List<Transaction> getTransactionsByCustomerId(long page, long size, TransactionType transactionType,
      boolean isAsc, MoneyTransferCriteria moneyTransferCriteria, Long customerId);
}
