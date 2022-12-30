package org.hcmus.premiere.repository.custom;


import java.util.Optional;
import org.hcmus.premiere.model.entity.User;

public interface CustomUserRepository {

  Optional<User> findUserByCreditCardNumber(String creditCardNumber);
}
