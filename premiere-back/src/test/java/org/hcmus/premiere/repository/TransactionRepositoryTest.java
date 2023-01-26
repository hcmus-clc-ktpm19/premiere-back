package org.hcmus.premiere.repository;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.model.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private CreditCardRepository creditCardRepository;

  @Autowired
  private BankRepository bankRepository;

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
        customerId,
        LocalDate.of(2022, 11, 1),
        LocalDate.now());

    // Then
    Assertions.assertThat(transactions).isNotEmpty();
  }

  @Test
  void testGetTransactionsByBankIdAndInRangeOfDate() {
    // Given
    Long bankId = 2L;
    LocalDate fromDate = LocalDate.now().minusDays(15);
    LocalDate toDate = LocalDate.now().plusDays(15);

    Transaction transaction = new Transaction();
    transaction.setAmount(BigDecimal.valueOf(anyLong()));
    transaction.setType(TransactionType.MONEY_TRANSFER);
    transaction.setTransactionRemark(anyString());
    transaction.setSenderBalance(BigDecimal.valueOf(anyLong()));
    transaction.setReceiverBalance(BigDecimal.valueOf(anyLong()));
    transaction.setSenderCreditCardNumber(creditCardRepository.findById(1L).get().getCardNumber());
    transaction.setReceiverCreditCardNumber(creditCardRepository.findById(2L).get().getCardNumber());
    transaction.setFee(BigDecimal.valueOf(anyLong()));
    transaction.setStatus(TransactionStatus.CHECKING);
    transaction.setSenderBank(bankRepository.findById(bankId).get());
    transaction.setReceiverBank(bankRepository.findById(bankId).get());
    transactionRepository.save(transaction);

    // When
    List<Transaction> transactions = transactionRepository.getTransactionsByBankIdAndInRangeOfDate(
        0,
        9,
        bankId,
        fromDate,
        toDate);

    // Then
    Assertions.assertThat(transactions).isNotEmpty();
  }
}