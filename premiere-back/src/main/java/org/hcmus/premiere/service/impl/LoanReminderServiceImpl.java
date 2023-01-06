package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.model.exception.LoanReminderNotFoundException.LOAN_REMINDER_NOT_FOUND;
import static org.hcmus.premiere.model.exception.LoanReminderNotFoundException.LOAN_REMINDER_NOT_FOUND_MESSAGE;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.dto.TransferMoneyRequestDto;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.model.entity.PremiereAbstractEntity;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.model.enums.LoanStatus;
import org.hcmus.premiere.model.enums.TransactionStatus;
import org.hcmus.premiere.model.enums.TransactionType;
import org.hcmus.premiere.model.exception.LoanReminderNotFoundException;
import org.hcmus.premiere.repository.LoanReminderRepository;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.LoanReminderService;
import org.hcmus.premiere.service.OTPService;
import org.hcmus.premiere.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class LoanReminderServiceImpl implements LoanReminderService {

  private final LoanReminderRepository loanReminderRepository;

  private final CreditCardService creditCardService;

  private final OTPService otpService;

  private final BankService bankService;

  private final TransactionService transactionService;



  @Override
  public Long saveLoanReminder(LoanReminder loanReminder) {
    loanReminderRepository.saveAndFlush(loanReminder);
    return loanReminder.getId();
  }

  @Override
  public List<LoanReminder> getLoanRemindersByUserCreditCardNumber(String userCreditCardNumber) {
    CreditCard userCreditCard = creditCardService.findCreditCardByNumber(userCreditCardNumber);
    List<LoanReminder> senderLoanReminders = loanReminderRepository.findAllBySenderCreditCard(userCreditCard);
    List<LoanReminder> receiverLoanReminders = loanReminderRepository.findAllByReceiverCreditCard(userCreditCard);
    return List.of(senderLoanReminders, receiverLoanReminders)
        .stream()
        .flatMap(List::stream)
        .sorted(Comparator.comparing(PremiereAbstractEntity::getId))
        .toList();
  }

  @Override
  public Long cancelLoanReminder(LoanReminderDto loanReminderDto) {
    // update loan reminder status to CANCELLED and cancel reason
    LoanReminder loanReminder = getLoanReminderById(loanReminderDto.getId());
    loanReminder.setStatus(LoanStatus.CANCELLED);
    loanReminder.setCancelReason(loanReminderDto.getCancelReason());
    loanReminderRepository.saveAndFlush(loanReminder);
    return loanReminder.getId();
  }

  @Override
  public LoanReminder getLoanReminderById(Long id) {
     return loanReminderRepository
        .findById(id)
        .orElseThrow(() ->
            new LoanReminderNotFoundException(LOAN_REMINDER_NOT_FOUND_MESSAGE,
                id.toString(), LOAN_REMINDER_NOT_FOUND));
  }

  @Override
  public void payLoanReminder(TransferMoneyRequestDto transferMoneyRequestDto) {
    LoanReminder loanReminder = getLoanReminderById(transferMoneyRequestDto.getRequestID());

    if(!otpService.verifyOTPRequestId(transferMoneyRequestDto.getOtp(), loanReminder.getReceiverCreditCard().getUser().getEmail(), loanReminder.getId())) {
      throw new IllegalArgumentException(Constants.OTP_IS_NOT_VALID);
    }

    Bank premiereBank = bankService.findBankByName(Constants.PREMIERE_BANK_NAME);

    Transaction transaction = new Transaction();
    transaction.setSenderCreditCardNumber(loanReminder.getReceiverCreditCard().getCardNumber());
    transaction.setReceiverCreditCardNumber(loanReminder.getSenderCreditCard().getCardNumber());
    transaction.setAmount(loanReminder.getLoanBalance());
    transaction.setType(TransactionType.LOAN);
    transaction.setSelfPaymentFee(true);
    transaction.setFee(loanReminder.getLoanBalance().multiply(Constants.TRANSACTION_FEE));
    transaction.setTransactionRemark(loanReminder.getLoanRemark());
    transaction.setSenderBank(premiereBank);
    transaction.setReceiverBank(premiereBank);
    transaction.setStatus(TransactionStatus.CHECKING);

    transactionService.internalTransfer(transaction);

    loanReminder.setStatus(LoanStatus.PAID);
    loanReminderRepository.saveAndFlush(loanReminder);
  }



}
