package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.TransactionRequestDto;

public interface ValidationService {
  Boolean validateTransactionRequest(TransactionRequestDto transactionRequestDto);

}
