package org.hcmus.premiere.model.exception;

import lombok.Getter;

@Getter
public class LoanReminderNotFoundException extends AbstractNotFoundException {

  public static final String LOAN_REMINDER_NOT_FOUND_MESSAGE = "Loan reminder not found";

  public static final String LOAN_REMINDER_NOT_FOUND = "LOAN_REMINDER.NOT_FOUND";

  public LoanReminderNotFoundException(String message, String id, String i18nPlaceHolder) {
    super(message, id, i18nPlaceHolder);
  }

}