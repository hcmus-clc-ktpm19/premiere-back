package org.hcmus.premiere.repository;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.entity.Bank;
import org.junit.jupiter.api.Test;

class BankRepositoryTest extends AbstractRepositoryTest {

  @Test
  void testGetAllBanks() {
    List<Bank> list = bankRepository.findAll();
    Assertions.assertThat(list).isNotEmpty();
  }
}