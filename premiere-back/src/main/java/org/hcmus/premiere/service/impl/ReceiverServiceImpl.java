package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.CreditCardNotFoundException.CREDIT_CARD_DISABLED;
import static org.hcmus.premiere.model.exception.CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND;
import static org.hcmus.premiere.model.exception.ReceiverNotFoundException.RECEIVER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.ReceiverNotFoundException.RECEIVER_NOT_FOUND_MESSAGE;
import static org.hcmus.premiere.model.exception.UserReceiverNotFoundException.USER_RECEIVER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.UserReceiverNotFoundException.USER_RECEIVER_NOT_FOUND_MESSAGE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.entity.UserReceiver;
import org.hcmus.premiere.model.enums.CardStatus;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
import org.hcmus.premiere.model.exception.ReceiverExisted;
import org.hcmus.premiere.model.exception.ReceiverNotFoundException;
import org.hcmus.premiere.model.exception.UserNotFoundException;
import org.hcmus.premiere.model.exception.UserReceiverNotFoundException;
import org.hcmus.premiere.repository.CreditCardRepository;
import org.hcmus.premiere.repository.ReceiverRepository;
import org.hcmus.premiere.repository.UserReceiverRepository;
import org.hcmus.premiere.repository.UserRepository;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.ReceiverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class ReceiverServiceImpl implements ReceiverService {

  private final ReceiverRepository receiverRepository;
  private final CreditCardService creditCardService;
  private final BankService bankService;
  private final UserRepository userRepository;
  private final UserReceiverRepository userReceiverRepository;
  private final CreditCardRepository creditCardRepository;

  @Override
  public List<Receiver> findAllReceiversByUserId(Long userId) {
    return receiverRepository.findAllByUserId(userId);
  }

  @Override
  public Receiver findReceiverById(Long id) {
    return receiverRepository
        .findById(id)
        .orElseThrow(
            () -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, id.toString(), RECEIVER_NOT_FOUND));
  }

  @Override
  public Receiver findReceiverByCardNumber(String cardNumber) {
    return receiverRepository
        .findByCardNumber(cardNumber)
        .orElseThrow(() -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, cardNumber, RECEIVER_NOT_FOUND));
  }

  @Override
  public Receiver findReceiverByNickname(String nickname) {
    return receiverRepository
        .findByNickname(nickname)
        .orElseThrow(() -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, nickname, RECEIVER_NOT_FOUND));
  }

  @Override
  public Receiver saveReceiverExternal(ReceiverDto receiverDto) {
    User user = userRepository
        .findById(receiverDto.getUserId())
        .orElseThrow(() -> new UserNotFoundException(
            UserNotFoundException.USER_NOT_FOUND_MESSAGE,
            receiverDto.getUserId().toString(),
            UserNotFoundException.USER_NOT_FOUND)
        );
    Bank bank = bankService.findBankByName(receiverDto.getBankName());
    Receiver receiver = receiverRepository.findByCardNumber(receiverDto.getCardNumber()).orElse(new Receiver());
    receiver.setCardNumber(receiverDto.getCardNumber());

    UserReceiver userReceiver;
    if (receiver.getId() == null) {
      userReceiver = new UserReceiver();
    } else {
      userReceiver = userReceiverRepository
          .getUserReceiverByUserIdAndReceiverId(user.getId(), receiver.getId())
          .orElse(new UserReceiver());
    }

    if (userReceiver.getUserReceiverPk().getReceiverId() != null) {
      throw new ReceiverExisted(
          ReceiverExisted.RECEIVER_EXISTED_MESSAGE,
          receiverDto.getCardNumber(),
          ReceiverExisted.RECEIVER_EXISTED);
    }

    userReceiver.setReceiver(receiver);
    userReceiver.setFullName(receiverDto.getFullName());
    if (StringUtils.isEmpty(receiverDto.getNickname())) {
      userReceiver.setNickname(userReceiver.getFullName());
    } else {
      userReceiver.setNickname(receiverDto.getNickname());
    }
    userReceiver.setUser(user);

    receiver.getUsers().add(userReceiver);
    receiver.setBank(bank);
    return receiverRepository.save(receiver);
  }

  @Override
  public Receiver saveReceiver(ReceiverDto receiverDto) {
    CreditCard creditCard = creditCardService.findCreditCardByNumber(receiverDto.getCardNumber());
    if (creditCard.getStatus() == CardStatus.DISABLED) {
      throw new CreditCardNotFoundException("Credit card is disabled", creditCard.getCardNumber(), CREDIT_CARD_DISABLED);
    }

    if (!creditCardRepository.existsByUserId(receiverDto.getUserId())) {
      throw new CreditCardNotFoundException("Credit card by user id not found", creditCard.getCardNumber(), CREDIT_CARD_NOT_FOUND);
    }

    User user = userRepository
        .findById(receiverDto.getUserId())
        .orElseThrow(() -> new UserNotFoundException(
            UserNotFoundException.USER_NOT_FOUND_MESSAGE,
            receiverDto.getUserId().toString(),
            UserNotFoundException.USER_NOT_FOUND)
        );
    Bank bank = bankService.findBankByName(receiverDto.getBankName());
    Receiver receiver = receiverRepository.findByCardNumber(receiverDto.getCardNumber()).orElse(new Receiver());
    receiver.setCardNumber(creditCard.getCardNumber());

    UserReceiver userReceiver;
    if (receiver.getId() == null) {
      userReceiver = new UserReceiver();
    } else {
      userReceiver = userReceiverRepository
          .getUserReceiverByUserIdAndReceiverId(user.getId(), receiver.getId())
          .orElse(new UserReceiver());
    }

    if (userReceiver.getUserReceiverPk().getReceiverId() != null) {
      throw new ReceiverExisted(
          ReceiverExisted.RECEIVER_EXISTED_MESSAGE,
          receiverDto.getCardNumber(),
          ReceiverExisted.RECEIVER_EXISTED);
    }

    userReceiver.setReceiver(receiver);
    userReceiver.setFullName(creditCard.getUser().getFirstName() + " " + creditCard.getUser().getLastName());
    if (StringUtils.isEmpty(receiverDto.getNickname())) {
      userReceiver.setNickname(userReceiver.getFullName());
    } else {
      userReceiver.setNickname(receiverDto.getNickname());
    }
    userReceiver.setUser(user);

    receiver.getUsers().add(userReceiver);
    receiver.setBank(bank);
    return receiverRepository.save(receiver);
  }

  @Override
  public Receiver updateReceiver(ReceiverDto receiverDto) {
    UserReceiver userReceiver = userReceiverRepository
        .getUserReceiverByUserIdAndReceiverId(receiverDto.getUserId(), receiverDto.getId())
        .orElseThrow(() -> new UserReceiverNotFoundException(
                USER_RECEIVER_NOT_FOUND_MESSAGE,
                receiverDto.getId().toString(),
                USER_RECEIVER_NOT_FOUND
            )
        );
    userReceiver.setNickname(receiverDto.getNickname());
    return userReceiverRepository.save(userReceiver).getReceiver();
  }

  @Override
  public void deleteReceiver(Long userId, String cardNumber) {
    Receiver receiver = receiverRepository
        .findByCardNumber(cardNumber)
        .orElseThrow(() -> new ReceiverNotFoundException(RECEIVER_NOT_FOUND_MESSAGE, cardNumber, RECEIVER_NOT_FOUND));
    UserReceiver userReceiver = userReceiverRepository
        .getUserReceiverByUserIdAndReceiverId(userId, receiver.getId())
        .orElseThrow(() -> new UserReceiverNotFoundException(
                USER_RECEIVER_NOT_FOUND_MESSAGE,
                receiver.getId().toString(),
                USER_RECEIVER_NOT_FOUND
            )
        );

    userReceiverRepository.delete(userReceiver);
    if (!userReceiverRepository.isAnyReceiverExists(receiver.getId())) {
      receiverRepository.delete(receiver);
    }
  }
}

