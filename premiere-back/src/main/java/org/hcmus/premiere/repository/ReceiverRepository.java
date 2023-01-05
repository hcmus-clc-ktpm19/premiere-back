package org.hcmus.premiere.repository;

import java.util.Optional;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.repository.custom.CustomReceiverRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long>, QuerydslPredicateExecutor<Receiver>,
    CustomReceiverRepository {
  Optional<Receiver> findByCardNumber(String cardNumber);
}