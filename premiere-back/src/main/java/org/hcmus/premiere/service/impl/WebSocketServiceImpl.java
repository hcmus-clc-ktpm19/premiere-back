package org.hcmus.premiere.service.impl;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.LoanReminderMessageDto;
import org.hcmus.premiere.service.WebSocketService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
  private final RabbitTemplate rabbitTemplate;
  private final SimpMessagingTemplate simpMessagingTemplate;
  @Override
  public void pushLoanReminderMessage(LoanReminderMessageDto loanReminderMessageDto) {
    rabbitTemplate.convertAndSend("loan-reminder-queue", loanReminderMessageDto);
  }

  @Override
  public void forwardToRelevantPeopleOfLoanReminder(LoanReminderMessageDto loanReminderMessageDto) {
    // send message to topic
    simpMessagingTemplate.convertAndSend("/topic/messages", loanReminderMessageDto);
  }
}
