package org.hcmus.premiere.repository.custom;

import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;

public interface TransactionRepositoryCustom {

  long count(TransactionType transactionType, Long customerId);

  List<Transaction> getTransactionsByCustomerId(long page, long size, TransactionType transactionType,
      boolean isAsc, Long customerId);
}
