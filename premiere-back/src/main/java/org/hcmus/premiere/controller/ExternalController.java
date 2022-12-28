package org.hcmus.premiere.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.common.consts.PremiereApiUrls;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PremiereApiUrls.PREMIERE_API_V2_EXTERNAL)
@RequiredArgsConstructor
public class ExternalController extends AbstractApplicationController {

  private final UserService userService;

  private final CreditCardService creditCardService;

  @GetMapping("/banks/customers")
  public List<UserDto> getUsersForExternalBank() {
    return userService
        .getUsers()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @GetMapping("/banks/credit-cards")
  public List<CreditCardDto> getCreditCardsForExternalBank() {
    return creditCardService
        .getCreditCardsIgnoreBalance()
        .stream()
        .map(creditCardMapper::toDto)
        .toList();
  }

  @GetMapping("/test")
  public String test() {
    return "test";
  }
}
