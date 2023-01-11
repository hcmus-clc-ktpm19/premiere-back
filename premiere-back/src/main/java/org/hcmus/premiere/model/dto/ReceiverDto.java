package org.hcmus.premiere.model.dto;

import lombok.Data;

@Data
public class ReceiverDto {
  private Long id;
  private String cardNumber;
  private String nickname;
  private String fullName;
  private Long userId;
  private String bankName;
}
