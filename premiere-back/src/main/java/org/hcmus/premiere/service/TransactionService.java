package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TransactionService {

  long getTotalPages(TransactionType transactionType, Long customerId, int size);

  long getTotalElements(TransactionType transactionType, Long customerId);

  @PreAuthorize("@SecurityUtils.isCustomer() or @SecurityUtils.isStaff()")
  List<Transaction> getTransactionsByCustomerId(
      long page,
      long size,
      TransactionType transactionType,
      boolean isAsc,
      Long customerId);
}
