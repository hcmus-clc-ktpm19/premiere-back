package org.hcmus.premiere.repository.custom.impl;

import java.util.Optional;
import org.hcmus.premiere.model.entity.QReceiver;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.QUserReceiver;
import org.hcmus.premiere.model.entity.UserReceiver;
import org.hcmus.premiere.repository.custom.CustomUserReceiverRepository;
import org.hcmus.premiere.repository.custom.PremiereAbstractCustomRepository;

public class CustomUserReceiverRepositoryImpl extends PremiereAbstractCustomRepository<UserReceiver> implements CustomUserReceiverRepository {


  @Override
  public Optional<UserReceiver> getUserReceiverByUserIdAndReceiverId(Long userId, Long receiverId) {
    return Optional.ofNullable(
        selectFrom(QUserReceiver.userReceiver)
            .innerJoin(QUserReceiver.userReceiver.user, QUser.user)
            .innerJoin(QUserReceiver.userReceiver.receiver, QReceiver.receiver)
            .where(QUser.user.id.eq(userId).and(QReceiver.receiver.id.eq(receiverId)))
            .fetchOne()
    );
  }
}
