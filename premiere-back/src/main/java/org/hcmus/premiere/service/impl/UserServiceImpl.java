package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND_MESSAGE;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.exception.UserNotFoundException;
import org.hcmus.premiere.repository.UserRepository;
import org.hcmus.premiere.service.UserService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE, id.toString(), USER_NOT_FOUND));
  }


}
