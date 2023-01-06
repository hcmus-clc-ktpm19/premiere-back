package org.hcmus.premiere.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import org.assertj.core.api.Assertions;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.model.exception.ReceiverExisted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
class ReceiverServicelTest {

  @Autowired
  private ReceiverService receiverService;

  @Test
  @WithMockUser(username = "admin", roles = {"PREMIERE_ADMIN"})
  void testSaveReceiver() {
    ReceiverDto receiverDto = new ReceiverDto();
    receiverDto.setCardNumber("1234567890123456");
    receiverDto.setNickname(anyString());
    receiverDto.setFullName(anyString());
    receiverDto.setBankName(anyString());
    receiverDto.setUserId(1L);
    receiverDto.setBankName("Premierebank");

    Receiver res = receiverService.saveReceiver(receiverDto);

    Assertions.assertThat(res).isNotNull();
  }

  @Test
  @WithMockUser(username = "admin", roles = {"PREMIERE_ADMIN"})
  void testSaveReceiverFail() {
    ReceiverDto receiverDto = new ReceiverDto();
    receiverDto.setCardNumber("1234567890123451");
    receiverDto.setNickname(anyString());
    receiverDto.setFullName(anyString());
    receiverDto.setBankName(anyString());
    receiverDto.setUserId(1L);
    receiverDto.setBankName("Premierebank");

    assertThrows(ReceiverExisted.class, () -> {
      Receiver res = receiverService.saveReceiver(receiverDto);
      receiverService.saveReceiver(receiverDto);
    });
  }
}