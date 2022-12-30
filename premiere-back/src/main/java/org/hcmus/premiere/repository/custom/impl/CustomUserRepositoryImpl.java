package org.hcmus.premiere.repository.custom.impl;

import java.util.Optional;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.repository.custom.CustomUserRepository;
import org.hcmus.premiere.repository.custom.PremiereAbstractCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomUserRepositoryImpl extends PremiereAbstractCustomRepository<User> implements CustomUserRepository {

  @Override
  public Optional<User> findUserByCreditCardNumber(String creditCardNumber) {
    return Optional.ofNullable(
        selectFrom(QUser.user)
            .leftJoin(QUser.user.creditCard, QCreditCard.creditCard)
            .fetchJoin()
            .where(QCreditCard.creditCard.cardNumber.eq(creditCardNumber))
            .fetchOne()
    );
  }
}
