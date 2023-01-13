package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.entity.Bank;

public interface BankService {
  Bank findBankById(Long id);
  Bank findBankByName(String bankName);
  List<Bank> getBanks();
}
