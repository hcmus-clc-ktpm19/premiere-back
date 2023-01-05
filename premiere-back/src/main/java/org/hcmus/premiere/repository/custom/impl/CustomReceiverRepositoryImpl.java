package org.hcmus.premiere.repository.custom.impl;

import java.util.List;
import java.util.Optional;
import org.hcmus.premiere.model.entity.QReceiver;
import org.hcmus.premiere.model.entity.QUser;
import org.hcmus.premiere.model.entity.QUserReceiver;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.repository.custom.CustomReceiverRepository;
import org.hcmus.premiere.repository.custom.PremiereAbstractCustomRepository;

public class CustomReceiverRepositoryImpl extends PremiereAbstractCustomRepository<Receiver> implements CustomReceiverRepository {

  @Override
  public List<Receiver> findAllByUserId(Long userId) {
    return selectFrom(QReceiver.receiver)
        .innerJoin(QReceiver.receiver.users, QUserReceiver.userReceiver)
        .innerJoin(QUserReceiver.userReceiver.user, QUser.user)
        .where(QUser.user.id.eq(userId))
        .fetch();
  }

  @Override
  public Optional<Receiver> findByNickname(String nickname) {
    return Optional.ofNullable(selectFrom(QReceiver.receiver)
        .innerJoin(QReceiver.receiver.users, QUserReceiver.userReceiver)
        .where(QUserReceiver.userReceiver.nickname.eq(nickname))
        .fetchOne());
  }
}
