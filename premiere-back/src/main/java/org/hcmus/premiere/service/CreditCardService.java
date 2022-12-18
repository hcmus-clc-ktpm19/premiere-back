package org.hcmus.premiere.service;

import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;

public interface CreditCardService {
  CreditCard findCreditCardById(Long id);

  CreditCard findCreditCardByNumber(String number);

  CreditCard findCreditCardByUserId(Long id);

  CreditCard saveCreditCard(User user);


}
