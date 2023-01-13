package org.hcmus.premiere.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.model.entity.UserReceiver;
import org.hcmus.premiere.service.ReceiverService;
import org.hcmus.premiere.service.UserReceiverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Receivers API")
@RestController
@RequestMapping("/api/v1/receivers")
@RequiredArgsConstructor
public class ReceiverController extends AbstractApplicationController{

  private final ReceiverService receiverService;

  private final UserReceiverService userReceiverService;

  @Operation(summary = "Get user's receivers by that user id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ReceiverDto.class))))
  })
  @GetMapping
  public ResponseEntity<List<ReceiverDto>> getReceiverByUserId(@RequestParam("userId") Long userId) {
    return ResponseEntity.ok(
        receiverService.findAllReceiversByUserId(userId)
            .stream()
            .map(receiver -> {
              UserReceiver userReceiver = userReceiverService.getUserReceiverByUserIdAndReceiverId(userId, receiver.getId());
              ReceiverDto receiverDto = applicationMapper.toReceiverDto(receiver);
              receiverDto.setNickname(userReceiver.getNickname());
              receiverDto.setFullName(userReceiver.getFullName());
              return receiverDto;
            })
            .toList());
  }

  @Operation(summary = "Get user's receivers by that user's credit card number")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ReceiverDto.class)))
  })
  @GetMapping("/{userId}/{receiverCardNumber}")
  public ResponseEntity<ReceiverDto> getReceiverByCardNumber(
      @PathVariable Long userId,
      @PathVariable String receiverCardNumber
  ) {
    Receiver receiver = receiverService.findReceiverByCardNumber(receiverCardNumber);
    UserReceiver userReceiver = userReceiverService.getUserReceiverByUserIdAndReceiverId(userId, receiver.getId());
    ReceiverDto receiverDto = applicationMapper.toReceiverDto(receiver);
    receiverDto.setNickname(userReceiver.getNickname());
    receiverDto.setFullName(userReceiver.getFullName());
    return ResponseEntity.ok(receiverDto);
  }


  @Operation(summary = "Save new receiver of a user by that user's id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ReceiverDto.class)))
  })
  @PostMapping
  public ResponseEntity<ReceiverDto> saveReceiver(@RequestBody ReceiverDto receiverDto) {
    return new ResponseEntity<>(applicationMapper.toReceiverDto(receiverService.saveReceiver(receiverDto)), CREATED);
  }

  @PostMapping("/external")
  public ResponseEntity<ReceiverDto> saveReceiverExternal(@RequestBody ReceiverDto receiverDto) {
    return new ResponseEntity<>(applicationMapper.toReceiverDto(receiverService.saveReceiverExternal(receiverDto)), CREATED);
  }

  @Operation(summary = "Update a receiver of a user by that user's id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ReceiverDto.class)))
  })
  @PutMapping
  public ResponseEntity<ReceiverDto> updateReceiver(@RequestBody ReceiverDto receiverDto) {
    return ResponseEntity.ok(applicationMapper.toReceiverDto(receiverService.updateReceiver(receiverDto)));
  }

  @Operation(summary = "Delete a receiver of a user by that user's id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "The receiver is deleted successfully but no content is returned")
  })
  @DeleteMapping("/{userId}/{receiverCardNumber}")
  @ResponseStatus(NO_CONTENT)
  public void deleteReceiver(
      @PathVariable Long userId,
      @PathVariable String receiverCardNumber) {
    receiverService.deleteReceiver(userId, receiverCardNumber);
  }
}
