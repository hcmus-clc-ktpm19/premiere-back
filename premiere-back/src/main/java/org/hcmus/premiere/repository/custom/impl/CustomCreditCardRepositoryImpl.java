package org.hcmus.premiere.repository.custom.impl;

import static org.hcmus.premiere.model.entity.QCreditCard.*;

import java.util.List;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.repository.custom.CustomCreditCardRepository;
import org.hcmus.premiere.repository.custom.PremiereAbstractCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomCreditCardRepositoryImpl extends PremiereAbstractCustomRepository<CreditCard> implements CustomCreditCardRepository {

  @Override
  public List<CreditCard> getCreditCardsIgnoreBalance() {
    return selectFrom(creditCard)
        .select(
            creditCard.id,
            creditCard.version,
            creditCard.createdAt,
            creditCard.updatedAt,
            creditCard.openDay,
            creditCard.cardNumber,
            creditCard.user
        )
        .where(creditCard.balance.gt(0))
        .fetch()
        .stream().map(tuple -> {
          CreditCard creditCard = new CreditCard();
          creditCard.setId(tuple.get(QCreditCard.creditCard.id));
          creditCard.setVersion(tuple.get(QCreditCard.creditCard.version));
          creditCard.setOpenDay(tuple.get(QCreditCard.creditCard.openDay));
          creditCard.setCardNumber(tuple.get(QCreditCard.creditCard.cardNumber));
          creditCard.setUser(tuple.get(QCreditCard.creditCard.user));
          creditCard.setCreatedAt(tuple.get(QCreditCard.creditCard.createdAt));
          creditCard.setUpdatedAt(tuple.get(QCreditCard.creditCard.updatedAt));
          return creditCard;
        })
        .toList();
  }
}
