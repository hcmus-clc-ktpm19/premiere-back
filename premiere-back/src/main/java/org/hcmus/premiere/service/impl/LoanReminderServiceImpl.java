package org.hcmus.premiere.service.impl;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.model.entity.PremiereAbstractEntity;
import org.hcmus.premiere.repository.LoanReminderRepository;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.LoanReminderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class LoanReminderServiceImpl implements LoanReminderService {

  private final LoanReminderRepository loanReminderRepository;
  private final CreditCardService creditCardService;

  @Override
  public Long saveLoanReminder(LoanReminder loanReminder) {
    loanReminderRepository.saveAndFlush(loanReminder);
    return loanReminder.getId();
  }

  @Override
  public List<LoanReminder> getLoanRemindersByUserCreditCardNumber(String userCreditCardNumber) {
    CreditCard userCreditCard = creditCardService.findCreditCardByNumber(userCreditCardNumber);
    List<LoanReminder> senderLoanReminders = loanReminderRepository.findAllBySenderCreditCard(userCreditCard);
    List<LoanReminder> receiverLoanReminders = loanReminderRepository.findAllByReceiverCreditCard(userCreditCard);
    return List.of(senderLoanReminders, receiverLoanReminders)
        .stream()
        .flatMap(List::stream)
        .sorted(Comparator.comparing(PremiereAbstractEntity::getId))
        .toList();
  }
}
