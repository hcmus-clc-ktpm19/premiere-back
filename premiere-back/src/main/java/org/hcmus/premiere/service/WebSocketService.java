package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.DepositMoneyRequestDto;
import org.hcmus.premiere.model.dto.LoanReminderMessageDto;

public interface WebSocketService {

  void pushLoanReminderMessage(LoanReminderMessageDto loanReminderMessageDto);

  void pushDepositMoneyMessage(DepositMoneyRequestDto depositMoneyRequestDto);

  void forwardToRelevantPeople(Object o);
}
