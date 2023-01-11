package org.hcmus.premiere.model.entity.pk;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserReceiverPk implements Serializable {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "receiver_id", nullable = false)
  private Long receiverId;
}
