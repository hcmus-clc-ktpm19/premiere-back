package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.BankNotFoundException.BANK_NOT_FOUND;
import static org.hcmus.premiere.model.exception.BankNotFoundException.BANK_NOT_FOUND_MESSAGE;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.exception.BankNotFoundException;
import org.hcmus.premiere.repository.BankRepository;
import org.hcmus.premiere.service.BankService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
  private final BankRepository bankRepository;

  @Override
  public Bank findBankById(Long id) {
    return bankRepository
        .findById(id)
        .orElseThrow(() -> new BankNotFoundException(BANK_NOT_FOUND_MESSAGE, id.toString(), BANK_NOT_FOUND));
  }

  @Override
  public Bank findBankByName(String bankName) {
    return bankRepository
        .findByBankName(bankName)
        .orElseThrow(() -> new BankNotFoundException(BANK_NOT_FOUND_MESSAGE, bankName, BANK_NOT_FOUND));
  }
}
