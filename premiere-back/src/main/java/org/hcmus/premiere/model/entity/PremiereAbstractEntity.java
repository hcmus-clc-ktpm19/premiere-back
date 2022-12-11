package org.hcmus.premiere.model.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class PremiereAbstractEntity {

  @Id
  @Column(name = "id", nullable = false, columnDefinition = "SERIAL")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Version
  @Column(name = "version", nullable = false, columnDefinition = "INT DEFAULT 0")
  protected Long version;
}
