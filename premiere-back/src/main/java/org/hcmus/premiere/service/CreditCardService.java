package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CreditCardService {

  CreditCard findCreditCardById(Long id);

  @PreAuthorize("@SecurityUtils.containsRoles('CUSTOMER', 'PREMIERE_ADMIN', 'EMPLOYEE')")
  CreditCard findCreditCardByNumber(String number);

  CreditCard findCreditCardByUserId(Long id);

  List<CreditCard> getCreditCardsIgnoreBalance();

  List<Object> getCreditCardsFromByExternalBankId(Long externalBankId);

  CreditCard saveCreditCard(User user);

  CreditCard updateCreditCard(CreditCard creditCard);
}
