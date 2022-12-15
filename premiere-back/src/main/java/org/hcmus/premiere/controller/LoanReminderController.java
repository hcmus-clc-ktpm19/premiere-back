package org.hcmus.premiere.controller;

import static org.hcmus.premiere.model.consts.PremiereApiUrls.PREMIERE_API_V1;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.service.LoanReminderService;
import org.hcmus.premiere.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PREMIERE_API_V1 + "/loan-reminders")
@RequiredArgsConstructor
public class LoanReminderController extends AbstractApplicationController {

  private final LoanReminderService loanReminderService;

  private final UserService userService;

  @GetMapping("/debtor/card-number")
  public UserDto getDebtor(@RequestParam(name = "q") String query) {
    return userMapper.toDto(userService.findUserByCreditCardNumber(query));
  }

  @PostMapping
  public Long saveLoanReminder(@RequestBody LoanReminderDto loanReminderDto) {
    LoanReminder loanReminder = loanReminderMapper.toEntity(loanReminderDto);
    return loanReminderService.saveLoanReminder(loanReminder);
  }
}
