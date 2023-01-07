package org.hcmus.premiere.controller;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @ApiResponse(responseCode = "202", description = "Resource not found",
      content = @Content(mediaType = "application/json", examples = @ExampleObject(
          value = """
              {
                "message": "Credit card not found",
                "i18nPlaceHolder": "CREDIT_CARD.NOT_FOUND"
              }"""
      ))
  )
  public ResponseEntity<ErrorDto> handleNotFoundException(AbstractNotFoundException e) {
    ErrorDto errorDto = new ErrorDto(e.getMessage(), e.getI18nPlaceHolder());

    return ResponseEntity
        .status(ACCEPTED)
        .body(errorDto);
  }

  @ExceptionHandler(AbstractExistedException.class)
  @ApiResponse(responseCode = "409", description = "Resource already existed",
      content = @Content(mediaType = "application/json", examples = @ExampleObject(
          value = """
              {
                "message": "Receiver already existed",
                "i18nPlaceHolder": "RECEIVER.EXISTED"
              }"""
      ))
  )
  public ResponseEntity<ErrorDto> handleExistedException(AbstractExistedException e) {
    log.error(e.getMessage(), e);
    ErrorDto errorDto = new ErrorDto(e.getMessage(), e.getI18nPlaceHolder());
    return ResponseEntity.status(CONFLICT).body(errorDto);
  }

  @ExceptionHandler(WrongPasswordException.class)
  @ApiResponse(responseCode = "400", description = "Wrong password",
      content = @Content(mediaType = "application/json", examples = @ExampleObject(
          value = """
              {
                "message": "Wrong password",
                "i18nPlaceHolder": "AUTH.WRONG_PASSWORD"
              }"""
      ))
  )
  @ApiResponse(responseCode = "400", description = "Wrong password")
  public ResponseEntity<ErrorDto> handleWrongPasswordException(WrongPasswordException e) {
    log.error(e.getMessage(), e);
    return ResponseEntity.status(BAD_REQUEST).body(new ErrorDto(e.getMessage(), e.getI18nPlaceHolder()));
  }

  @ExceptionHandler({IllegalArgumentException.class, IllegalRoleAssignException.class})
  @ApiResponse(responseCode = "400", description = "Assign illegal data",
      content = @Content(mediaType = "application/json", examples = @ExampleObject(
          value = """
              {
                "message": "Assign illegal data",
                "i18nPlaceHolder": "AUTH.ROLE.ASSIGN_ILLEGAL_EMPLOYEE_ROLE"
              }"""
      ))
  )
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
      IllegalArgumentException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put(ERROR_MESSAGE, e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
  @ApiResponse(responseCode = "400")
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    Map<String, String> response = new HashMap<>();
    response.put("code", "400");
    response.put("message", Objects.requireNonNull(e.getFieldError()).getDefaultMessage());

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(PremiereAbstractException.class)
  @ApiResponse(responseCode = "500", description = "Premiere unexpected error",
      content = @Content(mediaType = "application/json", examples = @ExampleObject(
          value = """
              {
                "message": "Runtime exception error message",
                "i18nPlaceHolder": "PREMIERE.UNEXPECTED_ERROR"
              }"""
      ))
  )
  public ResponseEntity<ErrorDto> handlePremiereAbstractException(PremiereAbstractException e) {
    ErrorDto errorDto = new ErrorDto(e.getMessage(), PremiereAbstractException.UNEXPECTED_ERROR);

    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .body(errorDto);
  }

  @ExceptionHandler(Throwable.class)
  @ApiResponse(responseCode = "500", description = "Unexpected error",
      content = @Content(mediaType = "application/json", examples = @ExampleObject(
          value = """
              {
                "message": "Check and uncheck exception error message",
                "i18nPlaceHolder": "PREMIERE.INTERNAL_SERVER_ERROR"
              }"""
      ))
  )
  public ResponseEntity<ErrorDto> handleUnexpectedException(Throwable e) {
    log.error("Unexpected error: {}", e.getMessage(), e);
    ErrorDto errorDto = new ErrorDto(e.getMessage(), PremiereAbstractException.INTERNAL_SERVER_ERROR);

    return ResponseEntity
        .internalServerError()
        .body(errorDto);
  }
}
