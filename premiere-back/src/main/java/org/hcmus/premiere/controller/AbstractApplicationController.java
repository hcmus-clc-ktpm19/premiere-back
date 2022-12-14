package org.hcmus.premiere.controller;

import org.hcmus.premiere.util.mapper.ApplicationMapper;
import org.hcmus.premiere.util.mapper.LoanReminderMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractApplicationController {
  @Autowired
  protected ApplicationMapper applicationMapper;

  @Autowired
  protected LoanReminderMapper loanReminderMapper;
}
