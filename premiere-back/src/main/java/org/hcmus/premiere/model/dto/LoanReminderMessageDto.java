package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class LoanReminderMessageDto implements Serializable {
  private String senderName;
  private Long senderId;
  private String receiverName;
  private Long receiverId;
  private String message;
}
