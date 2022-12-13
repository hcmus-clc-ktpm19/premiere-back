package org.hcmus.premiere.service;

import org.hcmus.premiere.model.entity.Bank;

public interface BankService {
  Bank findBankById(Long id);
}
