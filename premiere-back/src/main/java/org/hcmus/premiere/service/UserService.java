package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.hcmus.premiere.model.entity.User;

public interface UserService {

  List<User> getUsers();

  User findUserById(Long id);

  User findUserByCreditCardNumber(String creditCardNumber);

  User saveUser(RegisterAccountDto registerAccountDto);
}
