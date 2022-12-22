package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.dto.LoanReminderMessageDto;
import org.hcmus.premiere.model.entity.LoanReminder;

public interface LoanReminderService {

  Long saveLoanReminder(LoanReminder loanReminder);
  List<LoanReminder> getLoanRemindersByUserCreditCardNumber(String userCreditCardNumber);
  Long cancelLoanReminder(LoanReminderDto loanReminderDto);

}
