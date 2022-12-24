package org.hcmus.premiere.controller;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.common.Constants;
import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;
import org.hcmus.premiere.service.CheckingTransactionService;
import org.hcmus.premiere.service.TransactionService;
import org.hcmus.premiere.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController extends AbstractApplicationController{

  private TransactionService transactionService;

  private CheckingTransactionService checkingTransactionService;

  private ValidationService validationService;

  @PostMapping("/money-transfer/validate")
  public ResponseEntity<?> validateTransferMoney(@RequestBody @Valid TransactionRequestDto transactionRequestDto) {
    Map<String, String> response = new HashMap<>();
    if(validationService.validateTransactionRequest(transactionRequestDto)) {
      Long checkingTransactionId = checkingTransactionService.sendOTP(transactionRequestDto);
      if (checkingTransactionId != null) {
        response.put("checkingTransactionId", checkingTransactionId.toString());
        response.put("message", Constants.TRANSFER_VALIDATE_SUCCESSFUL);
        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.badRequest().build();
      }
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/money-transfer")
  public ResponseEntity<?> transferMoney(@RequestBody @Valid TransferMoneyRequestDto transferMoneyRequestDto) {
    transactionService.transfer(transferMoneyRequestDto);
    return ResponseEntity.ok(Constants.TRANSFER_SUCCESSFUL);
  }

}
