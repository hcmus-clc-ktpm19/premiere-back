package org.hcmus.premiere.controller;

import javax.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/admin")
  @RolesAllowed("PREMIERE_ADMIN")
  public String admin() {
    return "admin";
  }

  @GetMapping("/customer")
  @RolesAllowed("CUSTOMER")
  public String customer() {
    return "customer";
  }

  @GetMapping("/employee")
  @RolesAllowed("EMPLOYEE")
  public String employee() {
    return "employee";
  }
}
