package org.hcmus.premiere.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReceiverRepositoryTest {

  @Autowired
  private ReceiverRepository receiverRepository;

  @Test
  void testGetAllReceivers() {
    Assertions.assertThat(receiverRepository.findAll()).isNotEmpty();
  }
}