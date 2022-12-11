package org.hcmus.premiere.service;

import org.hcmus.premiere.model.entity.CreditCard;

public interface CreditCardService {
  CreditCard findCreditCardById(Long id);

  CreditCard findCreditCardByNumber(String number);

  CreditCard findCreditCardByUserId(Long id);


}
