package org.hcmus.premiere.repository;

import java.util.Optional;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.repository.custom.CustomCreditCardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long>,
    QuerydslPredicateExecutor<CreditCard>, CustomCreditCardRepository {

  Optional<CreditCard> findCreditCardByCardNumber(String number);

  Optional<CreditCard> findCreditCardByUser(User user);

}