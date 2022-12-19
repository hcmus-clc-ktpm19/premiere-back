package org.hcmus.premiere.service.impl;

import lombok.AllArgsConstructor;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
import org.hcmus.premiere.repository.CreditCardRepository;
import org.hcmus.premiere.service.CreditCardService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

  private final CreditCardRepository creditCardRepository;

  private final UserServiceImpl userService;

  @Override
  public CreditCard findCreditCardById(Long id) {
    return creditCardRepository
        .findById(id)
        .orElseThrow(
            () -> new CreditCardNotFoundException("Credit card with id not found", id.toString(),
                CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard findCreditCardByNumber(String number) {
    return creditCardRepository
        .findCreditCardByCardNumber(number)
        .orElseThrow(
            () -> new CreditCardNotFoundException("Credit card with number not found", number,
                CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard findCreditCardByUserId(Long id) {
    User user = userService.findUserById(id);
    return creditCardRepository
        .findCreditCardByUser(user)
        .orElseThrow(() -> new CreditCardNotFoundException("Credit card with userId not found",
            id.toString(), CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND));
  }
}
