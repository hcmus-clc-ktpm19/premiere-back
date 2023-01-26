package org.hcmus.premiere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.hcmus.premiere.model.entity.CreditCard;
import org.junit.jupiter.api.Test;

class CreditCardRepositoryTest extends AbstractRepositoryTest {

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