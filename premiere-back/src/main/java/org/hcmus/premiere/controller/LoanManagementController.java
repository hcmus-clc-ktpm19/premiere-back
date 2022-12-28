package org.hcmus.premiere.controller;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V1;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.CreateLoanReminderDto;
import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.service.LoanReminderService;
import org.hcmus.premiere.service.OTPService;
import org.hcmus.premiere.service.UserService;
import org.hcmus.premiere.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(PREMIERE_API_V1 + "/loan-management")
@RequiredArgsConstructor
public class LoanManagementController extends AbstractApplicationController {

  private final LoanReminderService loanReminderService;

  private final UserService userService;

  private final ValidationService validationService;

  private final OTPService otpService;


  @GetMapping("/user/card-number/{cardNumber}")
  public UserDto getDebtor(@PathVariable String cardNumber) {
    return userMapper.toDto(userService.findUserByCreditCardNumber(cardNumber));
  }

  @GetMapping("/loan-reminder/{userCreditCardNumber}")
  public ResponseEntity<List<LoanReminderDto>> getLoanRemindersByUserCreditCardNumber(@PathVariable String userCreditCardNumber) {
    List<LoanReminderDto> loanReminderDtos = loanReminderService
        .getLoanRemindersByUserCreditCardNumber(userCreditCardNumber)
        .stream()
        .map(applicationMapper::toLoanReminderDto)
        .toList();
    log.info("Loan reminders: {}", loanReminderDtos);
    return ResponseEntity.ok(loanReminderDtos);
  }

  @PutMapping("/loan-reminder/cancel")
  public ResponseEntity<Long> cancelLoanReminder(@RequestBody LoanReminderDto loanReminderDto) {
    return ResponseEntity.ok(loanReminderService.cancelLoanReminder(loanReminderDto));
  }

  @PostMapping
  public Long saveLoanReminder(@RequestBody @Valid CreateLoanReminderDto createLoanReminderDto) {
    LoanReminder loanReminder = loanReminderMapper.toEntity(createLoanReminderDto);
    return loanReminderService.saveLoanReminder(loanReminder);
  }

  @PostMapping("/loan-reminder/validate/{id}")
  public ResponseEntity<?> validateLoanReminder(@PathVariable Long id) {
    LoanReminder loanReminder = loanReminderService.getLoanReminderById(id);
    if(validationService.validateLoanReminderRequest(loanReminder)) {
      otpService.sendOTPEmail(loanReminder.getReceiverCreditCard().getUser().getEmail());
      return ResponseEntity.ok(Constants.TRANSFER_VALIDATE_SUCCESSFUL);
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/loan-reminder/pay")
  public ResponseEntity<?> payLoanReminder(@RequestBody @Valid TransferMoneyRequestDto transferMoneyRequestDto) {
    loanReminderService.payLoanReminder(transferMoneyRequestDto);
    return ResponseEntity.ok(Constants.TRANSFER_SUCCESSFUL);
  }


}
