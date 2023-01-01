package org.hcmus.premiere.controller;

import static org.springframework.http.HttpStatus.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.hcmus.premiere.model.dto.ErrorDto;
import org.hcmus.premiere.model.exception.AbstractExistedException;
import org.hcmus.premiere.model.exception.AbstractNotFoundException;
import org.hcmus.premiere.model.exception.IllegalRoleAssignException;
import org.hcmus.premiere.model.exception.PremiereAbstractException;
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
        .status(ACCEPTED)
        .body(errorDto);
  }

  @ExceptionHandler(AbstractExistedException.class)
  public ResponseEntity<Map<String, String>> handleRuntimeException(AbstractExistedException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage() + " " + e.getIdentify());

    return ResponseEntity.status(NOT_FOUND).body(response);
  }

  @ExceptionHandler(WrongPasswordException.class)
  public ResponseEntity<Map<String, String>> handleWrongPasswordException(WrongPasswordException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage() + " " + e.getIdentify());

    return ResponseEntity.status(NOT_FOUND).body(response);
  }

  @ExceptionHandler({IllegalArgumentException.class, IllegalRoleAssignException.class})
  public ResponseEntity<ErrorDto> handleIllegalArgumentException(PremiereAbstractException e) {
    return ResponseEntity
        .status(BAD_REQUEST)
        .body(new ErrorDto(e.getMessage(), e.getI18nPlaceHolder()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    Map<String, String> response = new HashMap<>();
    String msg = ex.getMessage();
    if (ex.getCause().getCause() instanceof SQLException e
        && e.getMessage().contains("Key")) {
      msg = e.getMessage().substring(e.getMessage().indexOf("Key"));
    }
    response.put("code", "409");
    response.put("message", msg);
    return ResponseEntity.status(CONFLICT).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put("code", "400");
    response.put("message", Objects.requireNonNull(e.getFieldError()).getDefaultMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  @ExceptionHandler(PremiereAbstractException.class)
  public ResponseEntity<ErrorDto> handlePremiereAbstractException(PremiereAbstractException e) {
    ErrorDto errorDto = new ErrorDto(e.getMessage(), PremiereAbstractException.UNEXPECTED_ERROR);

    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .body(errorDto);
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
