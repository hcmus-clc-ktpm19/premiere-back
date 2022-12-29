package org.hcmus.premiere.resource;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.hcmus.premiere.common.consts.PremiereApiUrls;
import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.util.security.SecurityUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExternalBankResourceTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ResteasyWebTarget resteasyWebTarget;

  @Autowired
  private SecurityUtils securityUtils;

  @Test
  void testGetCreditCardsFromByExternalBankByRestEasy() {
    String servletPath = PremiereApiUrls.PREMIERE_API_V2_EXTERNAL + "/banks/credit-cards";
    String credentialsTime = LocalDateTime.now(ZoneId.systemDefault()).toString();
    String zoneId = ZoneId.systemDefault().toString();
    String secretKey = securityUtils.getSecretKey();

    String authToken = securityUtils.hash(servletPath + credentialsTime + zoneId + secretKey);

    ExternalBankResource proxy = resteasyWebTarget.proxy(ExternalBankResource.class);

    List<CreditCardDto> creditCardDtos =
        proxy.getCreditCardsFromByExternalBankId(
                authToken,
                credentialsTime,
                zoneId
            )
            .stream()
            .map(creditCard -> objectMapper.convertValue(creditCard, CreditCardDto.class))
            .toList();

    assertThat(creditCardDtos)
        .isNotNull()
        .isNotEmpty()
        .allMatch(creditCardDto -> creditCardDto.getId() != null);
  }
}