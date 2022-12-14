package org.hcmus.premiere.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.common.consts.PremiereApiUrls;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.DepositMoneyExternalRequestDto;
import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.TransactionService;
import org.hcmus.premiere.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PremiereApiUrls.PREMIERE_API_V2_EXTERNAL)
@RequiredArgsConstructor
public class ExternalController extends AbstractApplicationController {

  private final UserService userService;

  private final CreditCardService creditCardService;

  private final TransactionService transactionService;

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

  @GetMapping("/banks/credit-cards/{creditCardNumber}")
  public CreditCardDto getCreditCardByNumberForExternalBank(@PathVariable String creditCardNumber) {
    return creditCardMapper.toDto(creditCardService.getCreditCardByNumberIgnoreBalance(creditCardNumber));
  }

  @PostMapping("/banks/transactions/money-transfer")
  public ResponseEntity<?> transferMoneyForExternalBank(@RequestBody DepositMoneyExternalRequestDto depositMoneyExternalRequestDto)
      throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    Map<String, String> response = new HashMap<>();
    response.put("rsaToken", transactionService.transferMoneyExternalBank(depositMoneyExternalRequestDto));
    return ResponseEntity.ok(response);
  }
}
