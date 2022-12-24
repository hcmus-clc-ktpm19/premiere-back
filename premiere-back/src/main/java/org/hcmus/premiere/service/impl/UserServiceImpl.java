package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.UserNotFoundException.USER_NOT_FOUND_MESSAGE;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcmus.premiere.model.dto.RegisterAccountDto;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.Gender;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
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
