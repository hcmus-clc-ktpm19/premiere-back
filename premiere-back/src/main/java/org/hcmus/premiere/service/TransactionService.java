package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.entity.Transaction;

public interface TransactionService {
  void transfer(TransactionRequestDto transactionRequestDto);

//  Transaction createTransaction(TransactionRequestDto transactionRequestDto);

  void sendOTP(String senderCardNumber);

}
