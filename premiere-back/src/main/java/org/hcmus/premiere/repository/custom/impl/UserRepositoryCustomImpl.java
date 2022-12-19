package org.hcmus.premiere.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.repository.custom.UserRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<User> findUserByCreditCardNumber(String creditCardNumber) {
    return Optional.ofNullable(new JPAQuery<User>(entityManager)
        .from(QUser.user)
        .leftJoin(QUser.user.creditCard, QCreditCard.creditCard)
        .fetchJoin()
        .where(QCreditCard.creditCard.cardNumber.eq(creditCardNumber))
        .fetchOne());
  }
}
