package org.hcmus.premiere.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hcmus.premiere.model.enums.WebSocketAction;

@Data
@AllArgsConstructor
public class WebSocketResponseDto<T> {
  private WebSocketAction action;
  private String message;
  private T data;
}
