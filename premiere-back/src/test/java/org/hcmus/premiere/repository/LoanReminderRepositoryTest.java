package org.hcmus.premiere.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LoanReminderRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private LoanReminderRepository loanReminderRepository;

  @Test
  void testGetAllLoanReminders() {
    Assertions.assertThat(loanReminderRepository.findAll()).isNotEmpty();
  }
}