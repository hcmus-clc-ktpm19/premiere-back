package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TransactionService {

  @PreAuthorize("@SecurityUtils.hasRole('CUSTOMER')")
  List<Transaction> getCustomerTransactionsById(Long customerId);
}
