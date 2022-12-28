package org.hcmus.premiere.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.common.consts.PremiereApiUrls;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
import org.hcmus.premiere.repository.CreditCardRepository;
import org.hcmus.premiere.service.CreditCardApiService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.util.CreditCardNumberGenerator;
import org.hcmus.premiere.util.security.SecurityUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

  private final CreditCardRepository creditCardRepository;

  private final UserServiceImpl userService;

  private final CreditCardNumberGenerator creditCardNumberGenerator;

  private final ObjectMapper objectMapper;

  private final SecurityUtils securityUtils;

  private final CreditCardApiService proxy;

  public CreditCardServiceImpl(
      CreditCardRepository creditCardRepository,
      UserServiceImpl userService,
      CreditCardNumberGenerator creditCardNumberGenerator,
      ObjectMapper objectMapper,
      ResteasyWebTarget resteasyWebTarget,
      SecurityUtils securityUtils) {
    this.creditCardRepository = creditCardRepository;
    this.userService = userService;
    this.creditCardNumberGenerator = creditCardNumberGenerator;
    this.objectMapper = objectMapper;
    this.securityUtils = securityUtils;
    this.proxy = resteasyWebTarget.proxy(CreditCardApiService.class);
  }

  @Override
  public CreditCard findCreditCardById(Long id) {
    return creditCardRepository
        .findById(id)
        .orElseThrow(
            () -> new CreditCardNotFoundException("Credit card with id not found", id.toString(),
                CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard findCreditCardByNumber(String number) {
    return creditCardRepository
        .findCreditCardByCardNumber(number)
        .orElseThrow(
            () -> new CreditCardNotFoundException("Credit card with number not found", number,
                CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard findCreditCardByUserId(Long id) {
    User user = userService.findUserById(id);
    return creditCardRepository
        .findCreditCardByUser(user)
        .orElseThrow(() -> new CreditCardNotFoundException("Credit card with userId not found",
            id.toString(), CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public List<CreditCard> getCreditCardsIgnoreBalance() {
    return creditCardRepository.getCreditCardsIgnoreBalance();
  }

  @Override
  public List<CreditCardDto> getCreditCardsFromByExternalBankId(Long externalBankId) {
    String servletPath = PremiereApiUrls.PREMIERE_API_V2_EXTERNAL + "/banks/credit-cards";
    String credentialsTime = LocalDateTime.now(ZoneId.systemDefault()).toString();
    String zoneId = ZoneId.systemDefault().toString();
    String secretKey = securityUtils.getSecretKey();

    String authToken = securityUtils.hash(servletPath + credentialsTime + zoneId + secretKey);

    return proxy.getCreditCardsFromByExternalBankId(
            authToken,
            credentialsTime,
            zoneId
        )
        .stream()
        .map(creditCard -> objectMapper.convertValue(creditCard, CreditCardDto.class))
        .toList();
  }

  @Override
  public CreditCard saveCreditCard(User user) {
    CreditCard creditCard = new CreditCard();
    creditCard.setCardNumber(creditCardNumberGenerator.generate(Constants.CREDIT_CARD_NUMBER_BIN, Constants.CREDIT_CARD_NUMBER_LENGTH));
    creditCard.setBalance(Constants.CREDIT_CARD_INITIAL_BALANCE);
    creditCard.setUser(user);
    creditCard.setOpenDay(LocalDateTime.now());
    return creditCardRepository.saveAndFlush(creditCard);
  }

  @Override
  public CreditCard updateCreditCard(CreditCard creditCard) {
    return creditCardRepository.saveAndFlush(creditCard);
  }
}
