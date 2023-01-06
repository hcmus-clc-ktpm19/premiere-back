package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND_MESSAGE;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcmus.premiere.model.dto.FullInfoUserDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.Gender;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
import org.hcmus.premiere.model.exception.UserNotFoundException;
import org.hcmus.premiere.repository.UserRepository;
import org.hcmus.premiere.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public User findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(
            () -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE, id.toString(), USER_NOT_FOUND));
  }

  @Override
  public User findUserByCreditCardNumber(String creditCardNumber) {
    if (StringUtils.isEmpty(creditCardNumber)) {
      throw new IllegalStateException("Credit card must not be empty");
    }

    return userRepository.findUserByCreditCardNumber(creditCardNumber).orElseThrow(
        () -> new CreditCardNotFoundException("Credit card not found", creditCardNumber,
            CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public boolean isUserExist(Long id) {
    return userRepository.existsById(id);
  }

  @Override
  public User saveUser(FullInfoUserDto fullInfoUserDto) {
    User user;

    if (fullInfoUserDto.getId() != null && isUserExist(fullInfoUserDto.getId())) {
      user = findUserById(fullInfoUserDto.getId());
    } else {
      user = new User();
    }
    user.setEmail(fullInfoUserDto.getEmail());
    user.setFirstName(fullInfoUserDto.getFirstName());
    user.setLastName(fullInfoUserDto.getLastName());
    user.setPhone(fullInfoUserDto.getPhone());
    user.setAddress(fullInfoUserDto.getAddress());
    user.setGender(Gender.valueOf(fullInfoUserDto.getGender()));
    user.setPanNumber(fullInfoUserDto.getPanNumber());
    return userRepository.saveAndFlush(user);
  }
}
