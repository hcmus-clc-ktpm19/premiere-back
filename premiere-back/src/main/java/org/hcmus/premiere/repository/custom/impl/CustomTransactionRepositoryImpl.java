package org.hcmus.premiere.repository.custom.impl;

import static org.hcmus.premiere.model.enums.MoneyTransferCriteria.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.hcmus.premiere.model.entity.QBank;
import org.hcmus.premiere.model.entity.QCreditCard;
import org.hcmus.premiere.model.entity.QTransaction;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.MoneyTransferCriteria;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.repository.custom.CustomTransactionRepository;
import org.hcmus.premiere.repository.custom.PremiereAbstractCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomTransactionRepositoryImpl extends PremiereAbstractCustomRepository<Transaction> implements CustomTransactionRepository {

  @Override
  public long count(
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria,
      Long customerId,
      LocalDate fromDate,
      LocalDate toDate) {
    return getTransactionsByCustomerIdQuery(transactionType, moneyTransferCriteria, customerId, fromDate, toDate)
        .select(QTransaction.transaction.count())
        .fetchFirst();
  }

  @Override
  public List<Transaction> getTransactionsByCustomerId(
      long page,
      long size,
      TransactionType transactionType,
      boolean isAsc,
      MoneyTransferCriteria moneyTransferCriteria,
      Long customerId,
      LocalDate fromDate,
      LocalDate toDate) {
    return getTransactionsByCustomerIdQuery(transactionType, moneyTransferCriteria, customerId, fromDate, toDate)
        .orderBy(isAsc ? QTransaction.transaction.createdAt.asc() : QTransaction.transaction.createdAt.desc())
        .limit(size)
        .offset(page * size)
        .fetch();
  }

  @Override
  public List<Transaction> getTransactionsByBankIdAndInRangeOfDate(
      long page,
      long size,
      Long bankId,
      LocalDate fromDate,
      LocalDate toDate) {
    QBank bank1 = new QBank("bank1");
    QBank bank2 = new QBank("bank2");
    return selectFrom(QTransaction.transaction)
        .innerJoin(QTransaction.transaction.senderBank, bank1)
        .innerJoin(QTransaction.transaction.receiverBank, bank2)
        .where(
            bank1.id.eq(bankId).or(bank2.id.eq(bankId))
                .and(QTransaction.transaction.createdAt.between(fromDate.atStartOfDay(), toDate.plusDays(1).atStartOfDay())))
        .limit(size)
        .offset(page * size)
        .fetch();
  }

  @Override
  public BigDecimal getTotalAmountInRangeOfDate(LocalDate fromDate, LocalDate toDate) {
    return selectFrom(QTransaction.transaction)
        .where(QTransaction.transaction.createdAt.between(fromDate.atStartOfDay(), toDate.atStartOfDay()))
        .select(QTransaction.transaction.amount.sum())
        .fetchFirst();
  }

  private JPAQuery<Transaction> getTransactionsByCustomerIdQuery(
      TransactionType transactionType,
      MoneyTransferCriteria moneyTransferCriteria,
      Long customerId,
      LocalDate fromDate,
      LocalDate toDate) {
    BooleanExpression whereClause = QUser.user.id.eq(customerId);

    if (moneyTransferCriteria == null) {
      whereClause = whereClause.and(
          QTransaction.transaction.senderCreditCardNumber
              .eq(QCreditCard.creditCard.cardNumber)
              .or(QTransaction.transaction.receiverCreditCardNumber
                  .eq(QCreditCard.creditCard.cardNumber)));
    } else if (INCOMING.equals(moneyTransferCriteria)) {
      whereClause = whereClause.and(
          QTransaction.transaction.receiverCreditCardNumber.eq(QCreditCard.creditCard.cardNumber));
    } else if (OUTGOING.equals(moneyTransferCriteria)) {
      whereClause = whereClause.and(
          QTransaction.transaction.senderCreditCardNumber.eq(QCreditCard.creditCard.cardNumber));
    }

    if (transactionType != null) {
      whereClause = whereClause.and(QTransaction.transaction.type.eq(transactionType));
    }

    if (fromDate != null && toDate != null) {
      whereClause = whereClause
          .and(QTransaction.transaction.createdAt
              .between(fromDate.atStartOfDay(), toDate.plusDays(1).atStartOfDay()));
    }

    return new JPAQuery<Transaction>(entityManager)
        .from(QTransaction.transaction, QCreditCard.creditCard)
        .innerJoin(QCreditCard.creditCard.user, QUser.user)
        .where(whereClause);
  }

  @Override
  public BigDecimal getTotalAmount() {
    return selectFrom(QTransaction.transaction)
        .select(QTransaction.transaction.amount.sum())
        .fetchFirst();
  }
}
