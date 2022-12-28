package org.hcmus.premiere.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
import org.hcmus.premiere.repository.CreditCardRepository;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.util.CreditCardNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

  private final CreditCardRepository creditCardRepository;

  private final UserServiceImpl userService;

  private CreditCardNumberGenerator creditCardNumberGenerator;

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

  @Override
  public List<CreditCard> getCreditCardsIgnoreBalance() {
    return creditCardRepository.getCreditCardsIgnoreBalance();
  }

  @Override
  public List<Object> getCreditCardsFromByExternalBankId(Long externalBankId) {
    return Collections.emptyList();
  }

  @Override
  public CreditCard saveCreditCard(User user) {
    CreditCard creditCard = new CreditCard();
    creditCard.setCardNumber(creditCardNumberGenerator.generate(Constants.CREDIT_CARD_NUMBER_BIN, Constants.CREDIT_CARD_NUMBER_LENGTH));
    creditCard.setBalance(Constants.CREDIT_CARD_INITIAL_BALANCE);
    creditCard.setUser(user);
    creditCard.setOpenDay(LocalDateTime.now());
    return creditCardRepository.saveAndFlush(creditCard);
  }

  @Override
  public CreditCard updateCreditCard(CreditCard creditCard) {
    return creditCardRepository.saveAndFlush(creditCard);
  }
}
