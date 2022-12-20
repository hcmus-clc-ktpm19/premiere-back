package org.hcmus.premiere.controller;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V1;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.CreateLoanReminderDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.service.LoanReminderService;
import org.hcmus.premiere.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PREMIERE_API_V1 + "/loan-management")
@RequiredArgsConstructor
public class LoanManagementController extends AbstractApplicationController {

  private final LoanReminderService loanReminderService;

  private final UserService userService;

  @GetMapping("/user/card-number/{cardNumber}")
  public UserDto getDebtor(@PathVariable String cardNumber) {
    return userMapper.toDto(userService.findUserByCreditCardNumber(cardNumber));
  }

  @PostMapping
  public Long saveLoanReminder(@RequestBody @Valid CreateLoanReminderDto createLoanReminderDto) {
    LoanReminder loanReminder = loanReminderMapper.toEntity(createLoanReminderDto);
    return loanReminderService.saveLoanReminder(loanReminder);
  }
}
