package org.hcmus.premiere.service;

import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Receiver;

public interface ReceiverService {
  Receiver findReceiverById(Long id);
  Receiver findReceiverByCardNumber(String cardNumber);
  Receiver findReceiverByNickname(String nickname);
  Receiver findReceiverByCardNumberAndNickname(String cardNumber, String nickname);
  Receiver saveReceiver(ReceiverDto receiverDto);
}
