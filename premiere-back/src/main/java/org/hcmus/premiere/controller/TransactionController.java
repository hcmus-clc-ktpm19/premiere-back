package org.hcmus.premiere.controller;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.PremierePaginationResponseDto;
import org.hcmus.premiere.model.dto.TransactionCriteriaDto;
import org.hcmus.premiere.model.dto.TransactionDto;
import org.hcmus.premiere.model.dto.TransactionRequestDto;
import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;
import org.hcmus.premiere.service.CheckingTransactionService;
import org.hcmus.premiere.service.TransactionService;
import org.hcmus.premiere.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(PREMIERE_API_V1 + "/transactions")
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

  @PostMapping("/users/{userId}/get-transactions")
  public PremierePaginationResponseDto<TransactionDto> getTransactionsByCustomerId(
      @Valid @RequestBody TransactionCriteriaDto criteriaDto,
      @PathVariable Long userId) {
    List<TransactionDto> transactionDtos = transactionService
        .getTransactionsByCustomerId(
            criteriaDto.getPage(),
            criteriaDto.getSize(),
            criteriaDto.getTransactionType(),
            criteriaDto.isAsc(),
            userId)
        .stream()
        .map(transactionMapper::toDto)
        .toList();

    PremierePaginationResponseDto<TransactionDto> res = applicationMapper.toDto(transactionDtos, criteriaDto);
    res.getMeta().getPagination().setTotalPages(
        transactionService.getTotalPages(criteriaDto.getTransactionType(), userId,
            criteriaDto.getSize()));
    res.getMeta().getPagination().setTotalElements(transactionService.getTotalElements(criteriaDto.getTransactionType(), userId));

    return res;
  }
}
