package org.hcmus.premiere.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.entity.Bank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class BankRepositoryTest {

  @Autowired
  private BankRepository bankRepository;

  @Test
  void testGetAllBanks() {
    List<Bank> list = bankRepository.findAll();
    Assertions.assertThat(list).isNotEmpty();
  }
}