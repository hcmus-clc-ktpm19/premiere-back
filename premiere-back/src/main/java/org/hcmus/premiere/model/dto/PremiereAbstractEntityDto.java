package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * A DTO for the {@link org.hcmus.premiere.model.entity.PremiereAbstractEntity} entity
 */
@Data
public class PremiereAbstractEntityDto implements Serializable {

  private Long id;
  private Long version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}