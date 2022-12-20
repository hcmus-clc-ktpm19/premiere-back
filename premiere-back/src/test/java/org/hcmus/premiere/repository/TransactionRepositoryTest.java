package org.hcmus.premiere.repository;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionRepositoryTest {

  @Autowired
  private TransactionRepository transactionRepository;

  @Test
  void testGetAllTransactions() {
    Assertions.assertThat(transactionRepository.findAll()).isEmpty();
  }

  @Test
  void testGetCustomerTransactionsById() {
    // Given
    Long customerId = 2L;
    // When
    List<Transaction> transactions = transactionRepository.getCustomerTransactionsById(customerId);

    // Then
    Assertions.assertThat(transactions).isNotEmpty();
  }
}