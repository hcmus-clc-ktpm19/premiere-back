package org.hcmus.premiere.service.impl;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.repository.LoanReminderRepository;
import org.hcmus.premiere.service.LoanReminderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class LoanReminderServiceImpl implements LoanReminderService {

  private final LoanReminderRepository loanReminderRepository;

  @Override
  public Long saveLoanReminder(LoanReminder loanReminder) {
    loanReminderRepository.saveAndFlush(loanReminder);
    return loanReminder.getId();
  }
}
