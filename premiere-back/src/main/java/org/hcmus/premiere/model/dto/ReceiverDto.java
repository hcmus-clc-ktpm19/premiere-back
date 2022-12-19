package org.hcmus.premiere.model.dto;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.hcmus.premiere.model.entity.Bank;
import org.hcmus.premiere.model.entity.User;

@Data
public class ReceiverDto {
  private Long id;
  private String cardNumber;
  private String nickname;
  private String fullName;
  private Long userId;
  private String bankName;
}
