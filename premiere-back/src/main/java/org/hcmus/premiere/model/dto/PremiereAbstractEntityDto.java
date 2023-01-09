package org.hcmus.premiere.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * A DTO for the {@link org.hcmus.premiere.model.entity.PremiereAbstractEntity} entity
 */
@Data
public class PremiereAbstractEntityDto implements Serializable {

  @Nullable
  private Long id;
  private Long version;
  @Nullable
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;
  @Nullable
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;
}