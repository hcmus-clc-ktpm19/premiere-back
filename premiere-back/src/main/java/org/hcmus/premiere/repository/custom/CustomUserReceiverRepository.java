package org.hcmus.premiere.repository.custom;

import java.util.Optional;
import org.hcmus.premiere.model.entity.UserReceiver;

public interface CustomUserReceiverRepository {

  Optional<UserReceiver> getUserReceiverByUserIdAndReceiverId(Long userId, Long receiverId);
}
