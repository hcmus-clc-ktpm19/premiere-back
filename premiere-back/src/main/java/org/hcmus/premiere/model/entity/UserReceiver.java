package org.hcmus.premiere.model.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Data;
import org.hcmus.premiere.model.entity.pk.UserReceiverPk;

@Data
@Entity
@Table(name = "user_receiver", schema = "premiere")
public class UserReceiver {

  @EmbeddedId
  private UserReceiverPk userReceiverPk = new UserReceiverPk();

  @ManyToOne
  @MapsId("userId")
  private User user;

  @ManyToOne
  @MapsId("receiverId")
  private Receiver receiver;

  @Basic
  @Column(name = "nickname", nullable = false, columnDefinition = "VARCHAR(255)")
  private String nickname;

  @Basic
  @Column(name = "full_name", nullable = false, columnDefinition = "VARCHAR(255)")
  private String fullName;
}
