package org.hcmus.premiere.repository.custom.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.model.entity.QTransaction;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.repository.custom.CustomTransactionRepository;
import org.hcmus.premiere.repository.custom.PremiereAbstractCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomTransactionRepositoryImpl extends PremiereAbstractCustomRepository<Transaction> implements CustomTransactionRepository {

  @Override
  public long count(TransactionType transactionType, Long customerId) {
    return getTransactionsByCustomerIdQuery(transactionType, customerId)
        .select(QTransaction.transaction.count())
        .fetchFirst();
  }

  @Override
  public List<Transaction> getTransactionsByCustomerId(long page, long size,
      TransactionType transactionType, boolean isAsc, Long customerId) {
    return getTransactionsByCustomerIdQuery(transactionType, customerId)
        .orderBy(isAsc ? QTransaction.transaction.createdAt.asc() : QTransaction.transaction.createdAt.desc())
        .limit(size)
        .offset(page * size)
        .fetch();
  }

  private JPAQuery<Transaction> getTransactionsByCustomerIdQuery(TransactionType transactionType, Long customerId) {
    BooleanExpression whereClause = QUser.user.id.eq(customerId);
    whereClause = whereClause.and(
        QTransaction.transaction.senderCreditCardNumber.eq(QCreditCard.creditCard.cardNumber)
            .or(QTransaction.transaction.receiverCreditCardNumber.eq(QCreditCard.creditCard.cardNumber)));

    if (transactionType != null) {
      whereClause = whereClause.and(QTransaction.transaction.type.eq(transactionType));
    }

    return new JPAQuery<Transaction>(entityManager)
        .from(QTransaction.transaction, QCreditCard.creditCard)
        .innerJoin(QCreditCard.creditCard.user, QUser.user)
        .where(whereClause);
  }
}
