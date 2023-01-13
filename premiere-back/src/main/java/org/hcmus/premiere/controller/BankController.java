package org.hcmus.premiere.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.BankDto;
import org.hcmus.premiere.service.BankService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BankController extends AbstractApplicationController {

  private final BankService bankService;

  @RequestMapping
  public List<BankDto> getBanks() {
    return bankService
        .getBanks()
        .stream()
        .map(bankMapper::toDto)
        .toList();
  }
}
