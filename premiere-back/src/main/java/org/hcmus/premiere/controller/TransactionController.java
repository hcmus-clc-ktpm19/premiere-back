package org.hcmus.premiere.controller;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V1;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.TransactionDto;
import org.hcmus.premiere.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PREMIERE_API_V1 + "/transactions")
@RequiredArgsConstructor
public class TransactionController extends AbstractApplicationController {

  private final TransactionService transactionService;

  @GetMapping("/user/{userId}")
  public List<TransactionDto> getCustomerTransactionsById(@PathVariable Long userId) {
    return transactionService
        .getCustomerTransactionsById(userId)
        .stream()
        .map(transactionMapper::toDto)
        .toList();
  }
}
