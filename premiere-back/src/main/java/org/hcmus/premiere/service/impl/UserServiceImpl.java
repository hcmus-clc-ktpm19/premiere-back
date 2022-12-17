package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND_MESSAGE;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.Gender;
import org.hcmus.premiere.model.enums.PremiereRole;
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
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE, id.toString()));
  }

  @Override
  public User saveUser(RegisterAccountDto registerAccountDto){
    User user = new User();
    user.setEmail(registerAccountDto.getEmail());
    user.setFirstName(registerAccountDto.getFirstName());
    user.setLastName(registerAccountDto.getLastName());
    user.setPhone(registerAccountDto.getPhone());
    user.setAddress(registerAccountDto.getAddress());
    user.setGender(Gender.valueOf(registerAccountDto.getGender()));
    user.setPanNumber(registerAccountDto.getPanNumber());
    return userRepository.saveAndFlush(user);
  }


}
