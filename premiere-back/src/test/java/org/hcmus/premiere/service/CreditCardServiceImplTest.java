package org.hcmus.premiere.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class CreditCardServiceImplTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ResteasyWebTarget resteasyWebTarget;

  @Autowired
  private CreditCardService creditCardService;

  @Test
  void testGetCreditCardsFromByExternalBankByRestEasy() {
    CreditCardApiService proxy = resteasyWebTarget.proxy(CreditCardApiService.class);

    List<CreditCardDto> creditCardDtos =
        proxy.getCreditCardsFromByExternalBankId(
            "be481f356a5e9ded90acdc121d0a7345",
            "2023-12-30T16:03:21.660444713",
            "Asia/Bangkok"
            )
            .stream()
            .map(creditCard -> objectMapper.convertValue(creditCard, CreditCardDto.class))
            .toList();

    Assertions.assertThat(creditCardDtos).isNotNull().isNotEmpty().allMatch(creditCardDto -> creditCardDto.getId() != null);
  }

  @Test
  void testGetCreditCardsFromByExternalBankId() {
    creditCardService.getCreditCardsFromByExternalBankId(1L);

    Assertions.assertThat(creditCardService.getCreditCardsFromByExternalBankId(1L)).isNotNull().isNotEmpty();
  }
}
