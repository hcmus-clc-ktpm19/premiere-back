package org.hcmus.premiere.repository;

import org.hcmus.premiere.util.test.container.PostgresContainerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(PostgresContainerExtension.class)
abstract class AbstractRepositoryTest {

  @Autowired
  protected BankRepository bankRepository;
  @Autowired
  protected CreditCardRepository creditCardRepository;
  @Autowired
  protected LoanReminderRepository loanReminderRepository;
  @Autowired
  protected ReceiverRepository receiverRepository;
  @Autowired
  protected TransactionRepository transactionRepository;
  @Autowired
  protected UserRepository userRepository;
}
