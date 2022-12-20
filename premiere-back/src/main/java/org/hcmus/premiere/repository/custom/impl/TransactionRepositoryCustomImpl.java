package org.hcmus.premiere.repository.custom.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.model.entity.QTransaction;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.repository.custom.TransactionRepositoryCustom;

public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Transaction> getTransactionsByCustomerId(long page, long size,
      TransactionType transactionType, boolean isAsc, Long customerId) {
    BooleanExpression whereClause =
        QTransaction.transaction.senderCreditCardNumber.eq(QCreditCard.creditCard.cardNumber).or(
            QTransaction.transaction.receiverCreditCardNumber.eq(
                QCreditCard.creditCard.cardNumber));

    if (transactionType != null) {
      whereClause = whereClause.and(QTransaction.transaction.type.eq(transactionType));
    }

    return new JPAQuery<Transaction>(entityManager)
        .from(QTransaction.transaction, QCreditCard.creditCard)
        .innerJoin(QCreditCard.creditCard.user, QUser.user)
        .on(QUser.user.id.eq(customerId))
        .where(whereClause)
        .orderBy(isAsc ? QTransaction.transaction.createdAt.asc() : QTransaction.transaction.createdAt.desc())
        .limit(size)
        .offset(page * size)
        .fetch();
  }
}
