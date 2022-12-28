package org.hcmus.premiere.repository;

import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.repository.custom.CustomTransactionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
    QuerydslPredicateExecutor<Transaction>, CustomTransactionRepository {

}