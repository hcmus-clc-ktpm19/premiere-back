package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TransactionService {

  long getTotalPages(int size);

  @PreAuthorize("@SecurityUtils.isCustomer() or @SecurityUtils.isStaff()")
  List<Transaction> getTransactionsByCustomerId(
      long page,
      long size,
      TransactionType transactionType,
      boolean isAsc,
      Long customerId);
}
