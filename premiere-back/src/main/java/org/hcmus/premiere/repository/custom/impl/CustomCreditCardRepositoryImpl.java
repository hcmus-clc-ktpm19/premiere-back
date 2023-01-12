package org.hcmus.premiere.repository.custom.impl;

import static org.hcmus.premiere.model.entity.QCreditCard.*;
import static org.hcmus.premiere.model.entity.QUser.user;

import com.querydsl.core.Tuple;
import java.util.List;
import java.util.Optional;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.repository.custom.CustomCreditCardRepository;
import org.hcmus.premiere.repository.custom.PremiereAbstractCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomCreditCardRepositoryImpl extends PremiereAbstractCustomRepository<CreditCard> implements CustomCreditCardRepository {

  @Override
  public boolean existsByUserId(Long userId) {
    return selectFrom(creditCard)
        .innerJoin(creditCard.user, user)
        .where(user.id.eq(userId))
        .fetchOne() != null;
  }

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

  @Override
  public Optional<CreditCard> getCreditCardByNumberIgnoreBalance(String number) {
    Tuple res = selectFrom(creditCard)
        .select(
            creditCard.id,
            creditCard.version,
            creditCard.createdAt,
            creditCard.updatedAt,
            creditCard.openDay,
            creditCard.cardNumber,
            creditCard.user
        )
        .where(creditCard.cardNumber.eq(number))
        .fetchOne();

    if (res == null) {
      return Optional.empty();
    } else {
      CreditCard creditCard = new CreditCard();
      creditCard.setId(res.get(QCreditCard.creditCard.id));
      creditCard.setVersion(res.get(QCreditCard.creditCard.version));
      creditCard.setOpenDay(res.get(QCreditCard.creditCard.openDay));
      creditCard.setCardNumber(res.get(QCreditCard.creditCard.cardNumber));
      creditCard.setUser(res.get(QCreditCard.creditCard.user));
      creditCard.setCreatedAt(res.get(QCreditCard.creditCard.createdAt));
      creditCard.setUpdatedAt(res.get(QCreditCard.creditCard.updatedAt));
      return Optional.of(creditCard);
    }
  }

  @Override
  public Optional<CreditCard> getCreditCardByUserId(Long userId) {
    return Optional.ofNullable(selectFrom(creditCard)
        .where(creditCard.user.id.eq(userId))
        .fetchOne());
  }
}
