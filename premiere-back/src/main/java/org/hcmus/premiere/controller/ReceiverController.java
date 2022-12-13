package org.hcmus.premiere.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.service.ReceiverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/receivers")
@RequiredArgsConstructor
public class ReceiverController extends AbstractApplicationController{
  private final ReceiverService receiverService;

  @GetMapping
  public ResponseEntity<List<ReceiverDto>> getReceiverByUserId(@RequestParam("userId") Long userId) {
    return ResponseEntity.ok(
        receiverService.findAllReceiverByUserId(userId)
            .stream()
            .map(applicationMapper::toReceiverDto)
            .toList());
  }

  @GetMapping("/{cardNumber}")
  public ResponseEntity<ReceiverDto> getReceiverByCardNumber(@PathVariable String cardNumber) {
    Receiver receiver = receiverService.findReceiverByCardNumber(cardNumber);
    return ResponseEntity.ok(applicationMapper.toReceiverDto(receiver));
  }

  @PostMapping
  public ResponseEntity<ReceiverDto> saveReceiver(@RequestBody ReceiverDto receiverDto) {
    return new ResponseEntity<>(applicationMapper.toReceiverDto(receiverService.saveReceiver(receiverDto)), CREATED);
  }

  @PutMapping
  public ResponseEntity<ReceiverDto> updateReceiver(@RequestBody ReceiverDto receiverDto) {
    return ResponseEntity.ok(applicationMapper.toReceiverDto(receiverService.updateReceiver(receiverDto)));
  }

  @DeleteMapping("/{cardNumber}")
  public ResponseEntity<Void> deleteReceiver(@PathVariable String cardNumber) {
    receiverService.deleteReceiver(cardNumber);
    return ResponseEntity.noContent().build();
  }
}
