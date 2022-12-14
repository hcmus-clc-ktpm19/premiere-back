package org.hcmus.premiere.repository;

import java.util.List;
import java.util.Optional;
import org.hcmus.premiere.model.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
  Optional<Receiver> findByCardNumber(String cardNumber);
  Optional<Receiver> findByNickname(String nickname);
  List<Receiver> findAllByUserId(Long userId);
}