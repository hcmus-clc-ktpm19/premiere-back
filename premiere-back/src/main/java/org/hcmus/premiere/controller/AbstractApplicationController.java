package org.hcmus.premiere.controller;

import org.hcmus.premiere.util.mapper.ApplicationMapper;
import org.hcmus.premiere.util.mapper.CreditCardMapper;
import org.hcmus.premiere.util.mapper.LoanReminderMapper;
import org.hcmus.premiere.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractApplicationController {

  @Autowired
  protected ApplicationMapper applicationMapper;

  @Autowired
  protected LoanReminderMapper loanReminderMapper;

  @Autowired
  protected UserMapper userMapper;

  @Autowired
  protected CreditCardMapper creditCardMapper;
}
