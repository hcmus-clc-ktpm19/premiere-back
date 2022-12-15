package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.Receiver;
import org.springframework.stereotype.Component;


@Component
public class ApplicationMapper {

  public CreditCardDto toCreditCardDto(CreditCard creditCard) {
    if (creditCard == null) {
      return null;
    } else {
      CreditCardDto creditCardDto = new CreditCardDto();
      creditCardDto.setCardNumber(creditCard.getCardNumber());
      creditCardDto.setBalance(creditCard.getBalance());
      return creditCardDto;
    }
  }

  public ReceiverDto toReceiverDto(Receiver receiver) {
    if (receiver == null) {
      return null;
    } else {
      ReceiverDto receiverDto = new ReceiverDto();
      receiverDto.setId(receiver.getId());
      receiverDto.setCardNumber(receiver.getCardNumber());
      receiverDto.setNickname(receiver.getNickname());
      receiverDto.setFullName(receiver.getFullName());
      receiverDto.setUserId(receiver.getUser().getId());
      receiverDto.setBankId(receiver.getBank().getId());
      return receiverDto;
    }
  }

}
