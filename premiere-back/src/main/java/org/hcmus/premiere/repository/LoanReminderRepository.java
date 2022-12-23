package org.hcmus.premiere.repository;

import java.util.List;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanReminderRepository extends JpaRepository<LoanReminder, Long> {
  List<LoanReminder> findAllBySenderCreditCard(CreditCard senderCreditCard);
  List<LoanReminder> findAllByReceiverCreditCard(CreditCard receiverCreditCard);
}