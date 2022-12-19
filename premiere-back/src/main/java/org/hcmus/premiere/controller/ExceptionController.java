package org.hcmus.premiere.controller;

import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.dto.ErrorDto;
import org.hcmus.premiere.model.exception.AbstractExistedException;
import org.hcmus.premiere.model.exception.AbstractNotFoundException;
import org.hcmus.premiere.model.exception.WrongPasswordException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
  private static final String ERROR_MESSAGE = "Error: ";

  @ExceptionHandler(AbstractNotFoundException.class)
  public ResponseEntity<ErrorDto> handleNotFoundException(AbstractNotFoundException e) {
    ErrorDto errorDto = new ErrorDto(e.getMessage(), e.getI18nPlaceHolder());

    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(errorDto);
  }

  @ExceptionHandler(AbstractExistedException.class)
  public ResponseEntity<Map<String, String>> handleRuntimeException(AbstractExistedException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage() + " " + e.getIdentify());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(WrongPasswordException.class)
  public ResponseEntity<Map<String, String>> handleWrongPasswordException(WrongPasswordException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage() + " " + e.getIdentify());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    Map<String, String> response = new HashMap<>();
    String msg = ex.getMessage();
    if (ex.getCause().getCause() instanceof SQLException) {
      SQLException e = (SQLException) ex.getCause().getCause();

      if (e.getMessage().contains("Key")) {
        msg = e.getMessage().substring(e.getMessage().indexOf("Key"));
      }
    }
    response.put("code", "409");
    response.put("message", msg);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put("code", "400");
    response.put("message", Objects.requireNonNull(e.getFieldError()).getDefaultMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Map<String, String>> handleTokenExpiredException(Throwable e) {
    String message = e.getMessage();
    log.error("Unexpected error: {}", message);

    return ResponseEntity
        .internalServerError()
        .body(Map.of(ERROR_MESSAGE, message));
  }
}
