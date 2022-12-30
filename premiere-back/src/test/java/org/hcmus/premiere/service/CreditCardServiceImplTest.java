package org.hcmus.premiere.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class CreditCardServiceImplTest {

  @Autowired
  private CreditCardService creditCardService;

  @Test
  void testGetCreditCardsFromByExternalBankId() {
    creditCardService.getCreditCardsFromExternalById(1L);

    Assertions.assertThat(creditCardService.getCreditCardsFromExternalById(1L)).isNotNull().isNotEmpty();
  }
}
