package org.hcmus.premiere.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.repository.TransactionRepository;
import org.hcmus.premiere.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  public List<Transaction> getCustomerTransactionsById(Long customerId) {
    return transactionRepository.getCustomerTransactionsById(customerId);
  }
}
