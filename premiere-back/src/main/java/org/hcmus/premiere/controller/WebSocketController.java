package org.hcmus.premiere.controller;

import static org.hcmus.premiere.common.consts.PremiereApiUrls.PREMIERE_API_V1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.dto.DepositMoneyRequestDto;
import org.hcmus.premiere.model.dto.LoanReminderMessageDto;
import org.hcmus.premiere.model.dto.WebSocketResponseDto;
import org.hcmus.premiere.model.enums.WebSocketAction;
import org.hcmus.premiere.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(PREMIERE_API_V1 + "/notification")
@CrossOrigin(origins = "*")
public class WebSocketController {

  private final WebSocketService webSocketService;

  /**
   * Receive message from queue then broadcast to all drivers
   */
  @RabbitListener(queues = {"${queue.loan-reminder-queue}"})
  public void forwardToRelevantPeopleOfLoanReminder(LoanReminderMessageDto loanReminderMessageDto) {
    log.info("Received loan reminder message from queue: {}", loanReminderMessageDto);
    webSocketService.forwardToRelevantPeople(loanReminderMessageDto);
  }

  @RabbitListener(queues = {"${queue.deposit-money-queue}"})
  public void forwardToRelevantPeopleOfDepositMoney(DepositMoneyRequestDto depositMoneyRequestDto) {
    log.info("Received deposit money message from queue: {}", depositMoneyRequestDto);
    webSocketService.forwardToRelevantPeople(new WebSocketResponseDto<>(
        WebSocketAction.DEPOSIT_MONEY,
        "Deposit money successfully",
        depositMoneyRequestDto
    ));
  }

  @PostMapping("/loan-reminder/message")
  public ResponseEntity<?> pushLoanReminderMessage(@RequestBody LoanReminderMessageDto loanReminderMessageDto) {
    webSocketService.pushLoanReminderMessage(loanReminderMessageDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/deposit-money/message")
  @ResponseStatus(HttpStatus.OK)
  public void pushDepositMoneyMessage(@RequestBody DepositMoneyRequestDto depositMoneyRequestDto) {
    webSocketService.pushDepositMoneyMessage(depositMoneyRequestDto);
  }

  @MessageMapping("/notify")
  @SendTo("/topic/messages") // send to all user subscribe to this topic
  public LoanReminderMessageDto reset(@Payload LoanReminderMessageDto message) {
    log.debug("Message from controller: {}", message);
    return message;
  }
}
