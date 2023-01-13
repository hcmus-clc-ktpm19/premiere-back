package org.hcmus.premiere.service.impl;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V2_EXTERNAL;
import static org.hcmus.premiere.common.consts.PremiereApiUrls.TAIXIUBANK_API;
import static org.hcmus.premiere.model.exception.CreditCardNotFoundException.CREDIT_CARD_DISABLED;
import static org.hcmus.premiere.model.exception.CreditCardNotFoundException.CREDIT_CARD_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hcmus.premiere.common.consts.Constants;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.dto.CreditCardExternalDto;
import org.hcmus.premiere.model.dto.CreditCardExternalResponseDto;
import org.hcmus.premiere.model.dto.CreditCardNumberDto;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.model.enums.CardStatus;
import org.hcmus.premiere.model.exception.CreditCardNotFoundException;
import org.hcmus.premiere.repository.CreditCardRepository;
import org.hcmus.premiere.resource.ExternalBankResource;
import org.hcmus.premiere.service.BankService;
import org.hcmus.premiere.service.CreditCardService;
import org.hcmus.premiere.service.KeycloakService;
import org.hcmus.premiere.service.UserService;
import org.hcmus.premiere.util.CreditCardNumberGenerator;
import org.hcmus.premiere.util.security.SecurityUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(rollbackFor = Throwable.class)
public class CreditCardServiceImpl implements CreditCardService {

  private final CreditCardRepository creditCardRepository;

  private final UserService userService;

  private final CreditCardNumberGenerator creditCardNumberGenerator;

  private final ObjectMapper objectMapper;

  private final SecurityUtils securityUtils;

  private final ExternalBankResource resource;

  private final BankService bankService;

  private KeycloakService keycloakService;

  public CreditCardServiceImpl(
      CreditCardRepository creditCardRepository,
      UserServiceImpl userService,
      CreditCardNumberGenerator creditCardNumberGenerator,
      ObjectMapper objectMapper,
      ResteasyWebTarget resteasyWebTarget,
      SecurityUtils securityUtils,
      BankService bankService) {
    this.creditCardRepository = creditCardRepository;
    this.userService = userService;
    this.creditCardNumberGenerator = creditCardNumberGenerator;
    this.objectMapper = objectMapper;
    this.securityUtils = securityUtils;
    this.resource = resteasyWebTarget.proxy(ExternalBankResource.class);
    this.bankService = bankService;
  }

  @Autowired
  public void setKeycloakService(KeycloakService keycloakService) {
    this.keycloakService = keycloakService;
  }

  public KeycloakService getKeycloakService() {
    return keycloakService;
  }

  @Override
  public boolean isCreditCardEnable(Long userId) {
    CreditCard creditCard = creditCardRepository
        .getCreditCardByUserId(userId)
        .orElseThrow(() -> new CreditCardNotFoundException(
            "Credit card with number not found",
            userId.toString(),
            CREDIT_CARD_NOT_FOUND));
    return CardStatus.AVAILABLE.equals(creditCard.getStatus());
  }

  @Override
  public CreditCard findCreditCardById(Long id) {
    return creditCardRepository
        .findById(id)
        .orElseThrow(() -> new CreditCardNotFoundException(
            "Credit card with id not found",
            id.toString(),
            CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard findCreditCardByNumber(String number) {
    return creditCardRepository
        .findCreditCardByCardNumber(number)
        .orElseThrow(
            () -> new CreditCardNotFoundException("Credit card with number not found", number,
                CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard findCreditCardByNumberExternal(String number) {
    return creditCardRepository
        .findCreditCardByCardNumber(number)
        .orElseThrow(
            () -> new CreditCardNotFoundException("Credit card with number not found", number,
                CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard findCreditCardByUserId(Long id) {
    User user = userService.findUserById(id);
    return creditCardRepository
        .findCreditCardByUser(user)
        .orElseThrow(() -> new CreditCardNotFoundException("Credit card with userId not found",
            id.toString(), CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public CreditCard getCreditCardByNumberIgnoreBalance(String cardNumber) {
    return creditCardRepository
        .getCreditCardByNumberIgnoreBalance(cardNumber)
        .orElseThrow(() ->
            new CreditCardNotFoundException(
                "Credit card with number not found",
                cardNumber,
                CREDIT_CARD_NOT_FOUND));
  }

  @Override
  public List<CreditCard> getCreditCardsIgnoreBalance() {
    return creditCardRepository.getCreditCardsIgnoreBalance();
  }

  @Override
  public List<CreditCardDto> getCreditCardsFromExternalById(Long externalBankId) {
    String servletPath = PREMIERE_API_V2_EXTERNAL + "/banks/credit-cards";
    String credentialsTime = LocalDateTime.now(ZoneId.systemDefault()).toString();
    String zoneId = ZoneId.systemDefault().toString();
    String secretKey = securityUtils.getSecretKey();

    String authToken = securityUtils.hash(servletPath + credentialsTime + zoneId + secretKey);

    return resource.getCreditCardsFromByExternalBankId(
            authToken,
            credentialsTime,
            zoneId
        )
        .stream()
        .map(creditCard -> objectMapper.convertValue(creditCard, CreditCardDto.class))
        .toList();
  }

  @Override
  public CreditCardDto getCreditCardByNumberAndExternalBankId(
      Long externalBankId,
      String cardNumber) {
    String servletPath = PREMIERE_API_V2_EXTERNAL + "/banks/credit-cards/" + cardNumber;
    String credentialsTime = LocalDateTime.now(ZoneId.systemDefault()).toString();
    String zoneId = ZoneId.systemDefault().toString();
    String secretKey = securityUtils.getSecretKey();

    String authToken = securityUtils.hash(servletPath + credentialsTime + zoneId + secretKey);

    return objectMapper.convertValue(
        resource.getCreditCardByNumberAndExternalBankId(
            authToken,
            credentialsTime,
            zoneId,
            cardNumber
        ),
        CreditCardDto.class
    );
  }

  @Override
  public CreditCard saveCreditCard(User user) {
    CreditCard creditCard = new CreditCard();
    creditCard.setCardNumber(
        creditCardNumberGenerator.generate(Constants.CREDIT_CARD_NUMBER_BIN,
        Constants.CREDIT_CARD_NUMBER_LENGTH)
    );
    creditCard.setBalance(Constants.CREDIT_CARD_INITIAL_BALANCE);
    creditCard.setUser(user);
    creditCard.setOpenDay(LocalDateTime.now());
    creditCard.setStatus(CardStatus.AVAILABLE);
    return creditCardRepository.saveAndFlush(creditCard);
  }

  @Override
  public CreditCard updateCreditCard(CreditCard creditCard) {
    if(creditCard.getStatus() == CardStatus.DISABLED) {
      throw new CreditCardNotFoundException("Credit card is disabled", creditCard.getCardNumber(), CREDIT_CARD_DISABLED);
    }
    return creditCardRepository.saveAndFlush(creditCard);
  }

  @Override
  public Long depositMoney(String username, String creditCardNumber, BigDecimal amount) {
    CreditCard creditCard = null;

    if (StringUtils.isNotBlank(username)) {
      creditCard = findCreditCardByUserId(keycloakService.getUserIdByUsername(username));
    } else if (StringUtils.isNotBlank(creditCardNumber)) {
      creditCard = findCreditCardByNumber(creditCardNumber);
    }

    if (creditCard == null) {
      throw new CreditCardNotFoundException("Credit card not found", null, CREDIT_CARD_NOT_FOUND);
    }

    if(creditCard.getStatus() == CardStatus.DISABLED) {
      throw new CreditCardNotFoundException("Credit card is disabled", creditCard.getCardNumber(), CREDIT_CARD_DISABLED);
    }

    creditCard.setBalance(creditCard.getBalance().add(amount));
    return creditCard.getId();
  }

  @Override
  public void disableCreditCard(String cardNumber) {
    CreditCard creditCard = findCreditCardByNumber(cardNumber);
    creditCard.setStatus(CardStatus.DISABLED);
    creditCardRepository.saveAndFlush(creditCard);
  }

  @Override
  public CreditCardExternalResponseDto getCreditCardByNumberExternalBank(String externalBankName,
      String cardNumber) {
    Bank bank = bankService.findBankByName(externalBankName);
    if (Constants.TAIXIU_BANK_NAME.equals(bank.getBankName())) {
      RestTemplate restTemplate = new RestTemplate();
      String servletPath = TAIXIUBANK_API + "/accounts/external/get-info";
      String secretKey = securityUtils.getExternalSecretKey();
      long credentialsTime = System.currentTimeMillis();
      CreditCardNumberDto creditCardNumberDto = new CreditCardNumberDto();
      creditCardNumberDto.setSlug(Constants.PREMIERE_SLUG);
      creditCardNumberDto.setAccountNumber(cardNumber);
      JSONObject jsonObject = new JSONObject(creditCardNumberDto);
      String data = jsonObject.toString();
      String msgToken = securityUtils.hashMd5(credentialsTime + data + secretKey);

      CreditCardExternalDto creditCardExternalDto = new CreditCardExternalDto();
      creditCardExternalDto.setAccountNumber(cardNumber);
      creditCardExternalDto.setTimestamp(credentialsTime);
      creditCardExternalDto.setMsgToken(msgToken);
      creditCardExternalDto.setSlug(Constants.PREMIERE_SLUG);

      HttpEntity<?> request = new HttpEntity<>(creditCardExternalDto, null);
      ResponseEntity<CreditCardExternalResponseDto> response = restTemplate.exchange(servletPath,
          HttpMethod.POST, request, CreditCardExternalResponseDto.class);
      return response.getBody();
    } else {
      return null;
    }
  }

  @Override
  public void enableCreditCard(String cardNumber) {
    CreditCard creditCard = findCreditCardByNumber(cardNumber);
    creditCard.setStatus(CardStatus.AVAILABLE);
    creditCardRepository.saveAndFlush(creditCard);
  }
}
