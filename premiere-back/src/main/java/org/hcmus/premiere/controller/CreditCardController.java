package org.hcmus.premiere.controller;

import javax.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.service.CreditCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/credit_card")
public class CreditCardController extends AbstractApplicationController {

  private CreditCardService creditCardService;

  @GetMapping("/{id}")
  //  @RolesAllowed("CUSTOMER")
  public CreditCard getCreditCardInfo(@PathVariable Long id) {
    return creditCardService.findCreditCardById(id);
  }

}
