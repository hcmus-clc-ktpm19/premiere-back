package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Receiver;
import org.springframework.stereotype.Component;


@Component
public class ApplicationMapper {

//  public CreditCard toCreditCardEntity(ActorDto actorDto) {
//    if (actorDto == null) {
//      return null;
//    } else {
//      Actor actor = new Actor();
//      actor.setId(actorDto.getId());
//      actor.setFirstName(actorDto.getFirstName());
//      actor.setLastName(actorDto.getLastName());
//      return actor;
//    }
//  }

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
      receiverDto.setBankName(receiver.getBank().getBankName());
      return receiverDto;
    }
  }

}
