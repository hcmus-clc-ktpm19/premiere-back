package org.hcmus.premiere.repository;

import java.util.Optional;
import org.hcmus.premiere.model.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
  Optional<Bank> findByBankName(String bankName);
}