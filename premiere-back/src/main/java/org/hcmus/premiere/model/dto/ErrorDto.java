package org.hcmus.premiere.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto implements Serializable {

  private String message;
  private String i18nPlaceHolder;
}
