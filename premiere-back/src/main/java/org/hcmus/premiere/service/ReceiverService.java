package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Receiver;

public interface ReceiverService {
  Receiver findReceiverById(Long id);
  Receiver findReceiverByCardNumber(String cardNumber);
  Receiver findReceiverByNickname(String nickname);
  Receiver saveReceiver(ReceiverDto receiverDto);
  List<Receiver> findAllReceiversByUserId(Long userId);
  Receiver updateReceiver(ReceiverDto receiverDto);
  void deleteReceiver(Long userId, String cardNumber);
}
