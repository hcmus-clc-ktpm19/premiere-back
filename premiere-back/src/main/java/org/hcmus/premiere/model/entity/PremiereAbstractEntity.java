package org.hcmus.premiere.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
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
