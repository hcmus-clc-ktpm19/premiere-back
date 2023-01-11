package org.hcmus.premiere.repository;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionRepositoryTest {

  @Autowired
  private TransactionRepository transactionRepository;

  @Test
  void testGetAllTransactions() {
    Assertions.assertThat(transactionRepository.findAll()).isNotEmpty();
  }

  @Test
  void testGetTransactionsByCustomerId() {
    // Given
    Long customerId = 2L;
    // When
    List<Transaction> transactions = transactionRepository.getTransactionsByCustomerId(
        0,
        9,
        TransactionType.MONEY_TRANSFER,
        true,
        MoneyTransferCriteria.OUTGOING,
        customerId);

    // Then
    Assertions.assertThat(transactions).isNotEmpty();
  }
}