package org.hcmus.premiere.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.DepositMoneyRequestDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.service.CreditCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/credit-card")
public class CreditCardController extends AbstractApplicationController {

  private CreditCardService creditCardService;

  @GetMapping("/{userId}")
  public ResponseEntity<CreditCardDto> getCreditCardInfo(@PathVariable Long userId) {
    return ResponseEntity.ok(applicationMapper.toCreditCardDto(creditCardService.findCreditCardByUserId(userId)));
  }

  @GetMapping("/card-number/{number}")
  public CreditCardDto getCreditCardByNumber(
      @RequestParam(required = false, defaultValue = "true") boolean ignoreBalance,
      @PathVariable String number) {
    CreditCard creditCard = creditCardService.findCreditCardByNumber(number);
    return ignoreBalance
        ? creditCardMapper.toDtoIgnoreBalance(creditCard)
        : creditCardMapper.toDto(creditCard);
  }

  @GetMapping("/external-bank/{externalBankId}")
  public List<CreditCardDto> getCreditCardsFromByExternalBankId(@PathVariable Long externalBankId) {
    return creditCardService.getCreditCardsFromExternalById(externalBankId);
  }

  @GetMapping("/external-bank/{externalBankName}/card-number/{cardNumber}")
  public CreditCardDto getCreditCardByNumberAndExternalBankId(
      @PathVariable Long externalBankName,
      @PathVariable String cardNumber) {
    return creditCardService.getCreditCardByNumberAndExternalBankId(externalBankName, cardNumber);
  }

  @PutMapping("/deposit-money")
  public Long depositMoney(@RequestBody DepositMoneyRequestDto depositMoneyRequestDto) {
    return creditCardService.depositMoney(
        depositMoneyRequestDto.getUsername(),
        depositMoneyRequestDto.getCreditCardNumber(),
        depositMoneyRequestDto.getAmount()
    );
  }
}
