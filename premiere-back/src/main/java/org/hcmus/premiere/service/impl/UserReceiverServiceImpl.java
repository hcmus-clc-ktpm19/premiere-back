package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.UserReceiverNotFoundException.USER_RECEIVER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.UserReceiverNotFoundException.USER_RECEIVER_NOT_FOUND_MESSAGE;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.entity.UserReceiver;
import org.hcmus.premiere.model.exception.UserReceiverNotFoundException;
import org.hcmus.premiere.repository.UserReceiverRepository;
import org.hcmus.premiere.service.UserReceiverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class UserReceiverServiceImpl implements UserReceiverService {

  private final UserReceiverRepository userReceiverRepository;

  @Override
  public UserReceiver getUserReceiverByUserIdAndReceiverId(Long userId, Long receiverId) {
    return userReceiverRepository
        .getUserReceiverByUserIdAndReceiverId(userId, receiverId)
        .orElseThrow(() -> new UserReceiverNotFoundException(USER_RECEIVER_NOT_FOUND_MESSAGE, receiverId.toString(), USER_RECEIVER_NOT_FOUND));
  }
}
