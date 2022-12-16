package org.hcmus.premiere.controller;

import java.util.Map;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.exception.AbstractExistedException;
import org.hcmus.premiere.model.exception.AbstractNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
  private static final String ERROR_MESSAGE = "Error: ";

  @ExceptionHandler(AbstractNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleRuntimeException(AbstractNotFoundException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage() + " " + e.getIdentify());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(AbstractExistedException.class)
  public ResponseEntity<Map<String, String>> handleRuntimeException(AbstractExistedException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage() + " " + e.getIdentify());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

//  @ExceptionHandler(Throwable.class)
//  public ResponseEntity<Map<String, String>> handleTokenExpiredException(Throwable e) {
//    String message = e.getMessage();
//    log.error("Unexpected error: {}", message);
//
//    return ResponseEntity
//        .internalServerError()
//        .body(Map.of(ERROR_MESSAGE, message));
//  }
}
