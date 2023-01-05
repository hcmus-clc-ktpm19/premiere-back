package org.hcmus.premiere.repository;

import java.util.Optional;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.entity.UserReceiver;
import org.hcmus.premiere.model.entity.pk.UserReceiverPk;
import org.hcmus.premiere.repository.custom.CustomUserReceiverRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReceiverRepository extends JpaRepository<UserReceiver, UserReceiverPk>,
    CustomUserReceiverRepository {

  Optional<UserReceiver> getUserReceiverByUserAndReceiver(User user, Receiver receiver);
}
