package org.hcmus.premiere.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.service.CreditCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/credit_card")
public class CreditCardController extends AbstractApplicationController {

  private CreditCardService creditCardService;

  @GetMapping("/{id}")
  //  @RolesAllowed("CUSTOMER")
  public CreditCard getCreditCardInfo(@PathVariable Long id) {
    return creditCardService.findCreditCardById(id);
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
}
