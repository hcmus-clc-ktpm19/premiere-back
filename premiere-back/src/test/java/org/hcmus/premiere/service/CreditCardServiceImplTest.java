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

  @Test
  void getCreditCardsFromByExternalBankId() {
    CreditCardApiService proxy = resteasyWebTarget.proxy(CreditCardApiService.class);

    List<CreditCardDto> creditCardDtos =
        proxy.getCreditCardsFromByExternalBankId()
            .stream()
            .map(creditCard -> objectMapper.convertValue(creditCard, CreditCardDto.class))
            .toList();

    Assertions.assertThat(creditCardDtos).isNotNull().isNotEmpty().allMatch(creditCardDto -> creditCardDto.getId() != null);
  }
}
