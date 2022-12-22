package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.LoanReminderMessageDto;

public interface WebSocketService {
  void pushLoanReminderMessage(LoanReminderMessageDto loanReminderMessageDto);
  void forwardToRelevantPeopleOfLoanReminder(LoanReminderMessageDto loanReminderMessageDto);
}
