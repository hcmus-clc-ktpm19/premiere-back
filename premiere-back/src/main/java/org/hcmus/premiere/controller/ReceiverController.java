package org.hcmus.premiere.controller;

import static org.springframework.http.HttpStatus.CREATED;

import lombok.RequiredArgsConstructor;
import org.hcmus.premiere.model.dto.ReceiverDto;
import org.hcmus.premiere.model.entity.Receiver;
import org.hcmus.premiere.service.ReceiverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/receivers")
@RequiredArgsConstructor
public class ReceiverController extends AbstractApplicationController{
  private final ReceiverService receiverService;

  @GetMapping("/{cardNumber}")
  public ResponseEntity<ReceiverDto> getReceiverByCardNumber(@PathVariable String cardNumber) {
    Receiver receiver = receiverService.findReceiverByCardNumber(cardNumber);
    return ResponseEntity.ok(applicationMapper.toReceiverDto(receiver));
  }

  @PostMapping
  public ResponseEntity<ReceiverDto> saveReceiver(@RequestBody ReceiverDto receiverDto) {
    return new ResponseEntity<>(applicationMapper.toReceiverDto(receiverService.saveReceiver(receiverDto)), CREATED);
  }
}
