package org.hcmus.premiere.service.impl;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.DepositMoneyRequestDto;
import org.hcmus.premiere.model.dto.LoanReminderMessageDto;
import org.hcmus.premiere.service.WebSocketService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

  @Value("${queue.loan-reminder-queue}")
  private String loanReminderQueue;

  @Value("${queue.deposit-money-queue}")
  private String depositMoneyQueue;

  private final RabbitTemplate rabbitTemplate;

  private final SimpMessagingTemplate simpMessagingTemplate;

  @Override
  public void pushLoanReminderMessage(LoanReminderMessageDto loanReminderMessageDto) {
    rabbitTemplate.convertAndSend(loanReminderQueue, loanReminderMessageDto);
  }

  @Override
  public void pushDepositMoneyMessage(DepositMoneyRequestDto depositMoneyRequestDto) {
    rabbitTemplate.convertAndSend(depositMoneyQueue, depositMoneyRequestDto);
  }

  @Override
  public void forwardToRelevantPeople(Object o) {
    // send message to topic
    simpMessagingTemplate.convertAndSend("/topic/messages", o);
  }
}
