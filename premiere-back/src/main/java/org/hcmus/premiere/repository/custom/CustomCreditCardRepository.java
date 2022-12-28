package org.hcmus.premiere.repository.custom;

import java.util.List;
import org.hcmus.premiere.model.entity.CreditCard;

public interface CustomCreditCardRepository {

  List<CreditCard> getCreditCardsIgnoreBalance();
}
