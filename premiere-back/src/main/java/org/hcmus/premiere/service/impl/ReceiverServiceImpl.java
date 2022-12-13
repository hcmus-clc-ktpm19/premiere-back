package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.ReceiverExistedException.RECEIVER_EXISTED_MESSAGE;
import static org.hcmus.premiere.model.exception.ReceiverNotFoundException.RECEIVER_NOT_FOUND_MESSAGE;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.exception.ReceiverExistedException;
import org.hcmus.premiere.model.exception.ReceiverNotFoundException;
import org.hcmus.premiere.repository.ReceiverRepository;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.ReceiverService;
import org.hcmus.premiere.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiverServiceImpl implements ReceiverService {

  private final ReceiverRepository receiverRepository;
  private final CreditCardService creditCardService;
  private final UserService userService;
  private final BankService bankService;

  @Override
  public Receiver findReceiverById(Long id) {
    return receiverRepository
        .findById(id)
        .orElseThrow(() -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, id.toString()));
  }

  @Override
  public Receiver findReceiverByCardNumber(String cardNumber) {
    return receiverRepository
        .findByCardNumber(cardNumber)
        .orElseThrow(() -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, cardNumber));
  }

  @Override
  public Receiver findReceiverByNickname(String nickname) {
    return receiverRepository
        .findByNickname(nickname)
        .orElseThrow(() -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, nickname));
  }

  @Override
  public Receiver findReceiverByCardNumberAndNickname(String cardNumber, String nickname) {
    return receiverRepository
        .findByCardNumberAndNickname(cardNumber, nickname)
        .orElseThrow(() -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, cardNumber));
  }

  @Override
  public Receiver saveReceiver(ReceiverDto receiverDto) {
    receiverRepository
        .findByCardNumber(receiverDto.getCardNumber())
        .ifPresent(
            receiver -> {
              throw new ReceiverExistedException(RECEIVER_EXISTED_MESSAGE, receiverDto.getCardNumber());
            });
    CreditCard creditCard = creditCardService.findCreditCardByNumber(receiverDto.getCardNumber());
    User user = userService.findUserById(receiverDto.getUserId());
    Bank bank = bankService.findBankById(receiverDto.getBankId());

    Receiver receiver = new Receiver();
    receiver.setCardNumber(creditCard.getCardNumber());
    receiver.setNickname(receiverDto.getNickname());
    receiver.setFullName(creditCard.getUser().getFirstName() + " " + creditCard.getUser().getLastName());
    receiver.setUser(user);
    receiver.setBank(bank);
    return receiverRepository.save(receiver);
  }
}

