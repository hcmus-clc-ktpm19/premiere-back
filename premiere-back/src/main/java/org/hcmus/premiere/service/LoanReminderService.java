package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.entity.LoanReminder;

public interface LoanReminderService {

  Long saveLoanReminder(LoanReminder loanReminder);
  List<LoanReminder> getLoanRemindersByUserCreditCardNumber(String userCreditCardNumber);
}