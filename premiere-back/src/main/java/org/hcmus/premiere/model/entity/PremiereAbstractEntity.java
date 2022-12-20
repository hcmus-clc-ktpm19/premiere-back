package org.hcmus.premiere.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class PremiereAbstractEntity {

  @Id
  @Column(name = "id", nullable = false, columnDefinition = "SERIAL")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Version
  @Column(name = "version", nullable = false, columnDefinition = "INT DEFAULT 0")
  protected Long version;

  // created at and update at
  @CreatedDate
  @Column(name = "created_at", updatable = false)
  protected LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  protected LocalDateTime updatedAt;
}
