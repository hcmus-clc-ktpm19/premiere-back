package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.TransactionRequestDto;

public interface TransactionService {
  void transfer(TransactionRequestDto transactionRequestDto);

}
