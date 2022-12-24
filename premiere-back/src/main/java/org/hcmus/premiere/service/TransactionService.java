package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;

public interface TransactionService {
  void transfer(TransferMoneyRequestDto transferMoneyRequestDto);

}
