package org.hcmus.premiere.service;

import java.util.List;
import org.hcmus.premiere.model.dto.FullInfoUserDto;
import org.hcmus.premiere.model.entity.User;

public interface UserService {

  List<User> getUsers();

  User findUserById(Long id);

  User findUserByCreditCardNumber(String creditCardNumber);

  boolean isUserExist(Long id);

  User saveUser(FullInfoUserDto fullInfoUserDto);
}
