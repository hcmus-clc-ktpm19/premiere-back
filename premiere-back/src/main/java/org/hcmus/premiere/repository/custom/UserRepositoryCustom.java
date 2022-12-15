package org.hcmus.premiere.repository.custom;


import java.util.Optional;
import org.hcmus.premiere.model.entity.User;

public interface UserRepositoryCustom {

  Optional<User> findUserByCreditCardNumber(String creditCardNumber);
}
