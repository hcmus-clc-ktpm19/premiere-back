package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.entity.LoanReminder;

public interface ValidationService {

  Boolean validateTransactionRequest(TransactionRequestDto transactionRequestDto);

  Boolean validateLoanReminderRequest(LoanReminder loanReminder);

}
