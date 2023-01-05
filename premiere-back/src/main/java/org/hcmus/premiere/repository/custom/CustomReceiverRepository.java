package org.hcmus.premiere.repository.custom;

import java.util.List;
import java.util.Optional;
import org.hcmus.premiere.model.entity.Receiver;

public interface CustomReceiverRepository {

  List<Receiver> findAllByUserId(Long userId);

  Optional<Receiver> findByNickname(String nickname);
}
