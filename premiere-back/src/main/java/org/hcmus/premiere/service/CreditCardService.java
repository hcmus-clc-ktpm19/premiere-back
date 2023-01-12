package org.hcmus.premiere.service;

import java.math.BigDecimal;
import java.util.List;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.CreditCardExternalResponseDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CreditCardService {

  CreditCard findCreditCardById(Long id);

  @PreAuthorize("@SecurityUtils.containsRoles('CUSTOMER', 'PREMIERE_ADMIN', 'EMPLOYEE')")
  CreditCard findCreditCardByNumber(String number);

  CreditCard findCreditCardByNumberExternal(String number);

  CreditCard findCreditCardByUserId(Long id);

  CreditCard getCreditCardByNumberIgnoreBalance(String cardNumber);

  List<CreditCard> getCreditCardsIgnoreBalance();

  List<CreditCardDto> getCreditCardsFromExternalById(Long externalBankId);

  CreditCardDto getCreditCardByNumberAndExternalBankId(Long externalBankId, String cardNumber);

  CreditCard saveCreditCard(User user);

  CreditCard updateCreditCard(CreditCard creditCard);

  Long depositMoney(String username, String creditCardNumber, BigDecimal amount);

  void disableCreditCard(String cardNumber);

  CreditCardExternalResponseDto getCreditCardByNumberExternalBank(String externalBankName, String cardNumber);
}
