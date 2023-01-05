package org.hcmus.premiere.service;

import org.hcmus.premiere.model.entity.UserReceiver;

public interface UserReceiverService {

  UserReceiver getUserReceiverByUserIdAndReceiverId(Long userId, Long receiverId);
}
