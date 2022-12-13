package org.hcmus.premiere.service.impl;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.repository.LoanReminderRepository;
import org.hcmus.premiere.service.LoanReminderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanReminderServiceImpl implements LoanReminderService {

  private final LoanReminderRepository loanReminderRepository;

  @Override
  public Long saveLoanReminder(LoanReminder loanReminder) {
    return null;
  }
}
