package org.hcmus.premiere.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;
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
  public long getTotalPages(TransactionType transactionType, Long customerId, int size) {
    long count = getTotalElements(transactionType, customerId);
    long totalPages = count / size;
    return totalPages + (count % size == 0 ? 0 : 1);
  }

  @Override
  public long getTotalElements(TransactionType transactionType, Long customerId) {
    return transactionRepository.count(transactionType, customerId);
  }

  @Override
  public List<Transaction> getTransactionsByCustomerId(long page, long size, TransactionType transactionType,
      boolean isAsc, Long customerId) {
    return transactionRepository.getTransactionsByCustomerId(
        page,
        size,
        transactionType,
        isAsc,
        customerId);
  }
}
