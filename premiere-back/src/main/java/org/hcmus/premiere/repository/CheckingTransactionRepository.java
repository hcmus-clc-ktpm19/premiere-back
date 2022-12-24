package org.hcmus.premiere.repository;

import org.hcmus.premiere.model.entity.CheckingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckingTransactionRepository extends JpaRepository<CheckingTransaction, Long> {

}