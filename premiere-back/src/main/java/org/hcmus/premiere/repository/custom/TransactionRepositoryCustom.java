package org.hcmus.premiere.repository.custom;

import java.util.List;
import org.hcmus.premiere.model.entity.Transaction;

public interface TransactionRepositoryCustom {

  List<Transaction> getCustomerTransactionsById(Long customerId);
}
