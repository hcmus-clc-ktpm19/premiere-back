package org.hcmus.premiere.repository;

import java.util.Optional;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
  Optional<CreditCard> findCreditCardByCardNumber(String number);

  Optional<CreditCard> findCreditCardByUser(User user);

}