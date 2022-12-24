package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.entity.CheckingTransaction;

public interface CheckingTransactionService {

  CheckingTransaction getCheckingTransactionById(Long id);
  Long sendOTP(TransactionRequestDto transactionRequestDto);

}
