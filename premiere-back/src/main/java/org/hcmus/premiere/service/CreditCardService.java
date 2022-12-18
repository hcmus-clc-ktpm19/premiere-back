package org.hcmus.premiere.service;

import org.hcmus.premiere.model.entity.CreditCard;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CreditCardService {

  CreditCard findCreditCardById(Long id);

  @PreAuthorize("@SecurityUtils.containsRoles('CUSTOMER', 'PREMIERE_ADMIN', 'EMPLOYEE')")
  CreditCard findCreditCardByNumber(String number);

  CreditCard findCreditCardByUserId(Long id);
}
