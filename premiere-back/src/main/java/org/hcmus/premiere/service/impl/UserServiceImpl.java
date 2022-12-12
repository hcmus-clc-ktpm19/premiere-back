package org.hcmus.premiere.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.repository.UserRepository;
import org.hcmus.premiere.service.UserService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }
}
