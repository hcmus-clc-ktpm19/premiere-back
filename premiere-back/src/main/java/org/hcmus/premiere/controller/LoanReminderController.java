package org.hcmus.premiere.controller;

import static org.hcmus.premiere.model.consts.PremiereApiUrls.PREMIERE_API_V1;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.LoanReminderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PREMIERE_API_V1 + "/loan-reminders")
@RequiredArgsConstructor
public class LoanReminderController extends AbstractApplicationController {

  private final LoanReminderService loanReminderService;

  private final CreditCardService creditCardService;

  @PostMapping
  public Long saveLoanReminder(@RequestBody LoanReminderDto loanReminderDto) {
    LoanReminder loanReminder = loanReminderMapper.toEntity(loanReminderDto);
    return loanReminderService.saveLoanReminder(loanReminder);
  }
}
