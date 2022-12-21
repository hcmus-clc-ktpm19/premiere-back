package org.hcmus.premiere.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.common.Constants;
import org.hcmus.premiere.model.dto.TransactionRequestDto;
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

  private ValidationService validationService;

  @PostMapping("/money-transfer/validate")
  public ResponseEntity<?> validateTransferMoney(@RequestBody @Valid TransactionRequestDto transactionRequestDto) {
    if(validationService.validateTransactionRequest(transactionRequestDto)) {
      transactionService.sendOTP(transactionRequestDto.getSenderCardNumber());
    }

    return ResponseEntity.ok(Constants.TRANSFER_VALIDATE_SUCCESSFUL);
  }

  @PostMapping("/money-transfer")
  public ResponseEntity<?> transferMoney(@RequestBody @Valid TransactionRequestDto transactionRequestDto) {
    if(validationService.validateOtpTransactionRequest(transactionRequestDto)) {
      transactionService.transfer(transactionRequestDto);
    }
    return ResponseEntity.ok(Constants.TRANSFER_SUCCESSFUL);
  }

}
