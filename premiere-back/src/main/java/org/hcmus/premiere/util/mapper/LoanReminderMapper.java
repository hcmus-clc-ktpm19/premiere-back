package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.hcmus.premiere.service.CreditCardService;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public abstract class LoanReminderMapper {

  @Autowired
  protected CreditCardService creditCardService;

  @Mapping(target = "senderCreditCard", expression = "java(creditCardService.findCreditCardById(loanReminderDto.senderCreditCardId()))")
  @Mapping(target = "receiverCreditCard", expression = "java(creditCardService.findCreditCardById(loanReminderDto.receiverCreditCardId()))")
  @Mapping(target = "time", ignore = true)
  public abstract LoanReminder toEntity(LoanReminderDto loanReminderDto);

  @InheritInverseConfiguration(name = "toEntity")
  public abstract LoanReminderDto toDto(LoanReminder loanReminder);

  @Mapping(target = "senderCreditCard", expression = "java(creditCardService.findCreditCardById(loanReminderDto.senderCreditCardId()))")
  @Mapping(target = "receiverCreditCard", expression = "java(creditCardService.findCreditCardById(loanReminderDto.receiverCreditCardId()))")
  @InheritConfiguration(name = "toEntity")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract LoanReminder partialUpdate(
      LoanReminderDto loanReminderDto, @MappingTarget LoanReminder loanReminder);
}
