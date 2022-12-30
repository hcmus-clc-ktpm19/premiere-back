package org.hcmus.premiere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.hcmus.premiere.model.entity.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CreditCardRepositoryTest {

  @Autowired
  private CreditCardRepository creditCardRepository;

  @Test
  void testGetAllCreditCards() {
    assertThat(creditCardRepository.findAll()).isNotEmpty();
  }

  @Test
  void testGetAllCreditCardsIgnoreBalance() {
    List<CreditCard> creditCards = creditCardRepository.getCreditCardsIgnoreBalance();
    assertThat(creditCards)
        .isNotEmpty()
        .allMatch(creditCard -> creditCard.getBalance() == null);
  }
}