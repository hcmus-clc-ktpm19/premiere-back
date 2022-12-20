package org.hcmus.premiere.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.model.entity.QTransaction;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.repository.custom.TransactionRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Transaction> getCustomerTransactionsById(Long customerId) {
    return new JPAQuery<Transaction>(entityManager)
        .from(QTransaction.transaction)
        .where(QTransaction.transaction.senderCreditCardNumber.eq(
            new JPAQuery<QCreditCard>(entityManager)
                .select(QCreditCard.creditCard.cardNumber)
                .from(QCreditCard.creditCard)
                .innerJoin(QCreditCard.creditCard.user, QUser.user)
                .on(QUser.user.id.eq(customerId))
                .fetchOne()
        ).or(QTransaction.transaction.receiverCreditCardNumber.eq(
            new JPAQuery<QCreditCard>(entityManager)
                .select(QCreditCard.creditCard.cardNumber)
                .from(QCreditCard.creditCard)
                .innerJoin(QCreditCard.creditCard.user, QUser.user)
                .on(QUser.user.id.eq(customerId))
                .fetchOne()
        )))
        .fetch();
  }
}
