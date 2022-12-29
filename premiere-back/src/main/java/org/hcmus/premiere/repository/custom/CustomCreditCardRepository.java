package org.hcmus.premiere.repository.custom;

import java.util.List;
import java.util.Optional;
import org.hcmus.premiere.model.entity.CreditCard;

public interface CustomCreditCardRepository {

  List<CreditCard> getCreditCardsIgnoreBalance();

  Optional<CreditCard> getCreditCardByNumberIgnoreBalance(String number);
}
