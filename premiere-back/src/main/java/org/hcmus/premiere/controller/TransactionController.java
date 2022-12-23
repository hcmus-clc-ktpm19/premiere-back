package org.hcmus.premiere.controller;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V1;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.PremierePaginationReponseDto;
import org.hcmus.premiere.model.dto.TransactionCriteriaDto;
import org.hcmus.premiere.model.dto.TransactionDto;
import org.hcmus.premiere.service.TransactionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PREMIERE_API_V1 + "/transactions")
@RequiredArgsConstructor
public class TransactionController extends AbstractApplicationController {

  private final TransactionService transactionService;

  @PostMapping("/users/{userId}/get-transactions")
  public PremierePaginationReponseDto<TransactionDto> getTransactionsByCustomerId(
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

    PremierePaginationReponseDto<TransactionDto> res = applicationMapper.toDto(transactionDtos, criteriaDto);
    res.getMeta().getPagination().setTotalPages(
        transactionService.getTotalPages(criteriaDto.getTransactionType(), userId,
            criteriaDto.getSize()));
    res.getMeta().getPagination().setTotalElements(transactionService.getTotalElements(criteriaDto.getTransactionType(), userId));

    return res;
  }
}
