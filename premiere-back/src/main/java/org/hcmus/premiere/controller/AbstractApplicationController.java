package org.hcmus.premiere.controller;

import org.hcmus.premiere.util.mapper.ApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractApplicationController {
  @Autowired
  protected ApplicationMapper applicationMapper;
}
