package org.hcmus.premiere.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void testGetAllUsers() {
    Assertions.assertThat(userRepository.findAll()).isNotEmpty();
  }
}