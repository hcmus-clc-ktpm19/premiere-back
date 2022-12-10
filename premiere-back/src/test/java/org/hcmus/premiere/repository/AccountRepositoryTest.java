package org.hcmus.premiere.repository;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
class AccountRepositoryTest {

  @Autowired
  private AccountRepository accountRepository;

  @Test
  void testGetAllAccounts() {
    List<Account> accounts = accountRepository.findAll();

    Assertions.assertThat(accounts).isNotEmpty();
  }
}