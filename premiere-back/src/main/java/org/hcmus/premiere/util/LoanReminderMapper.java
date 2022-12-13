package org.hcmus.premiere.util;

import org.hcmus.premiere.model.dto.LoanReminderDto;
import org.hcmus.premiere.model.entity.LoanReminder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface LoanReminderMapper {

  LoanReminder toEntity(LoanReminderDto loanReminderDto);

  LoanReminderDto toDto(LoanReminder loanReminder);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  LoanReminder partialUpdate(
      LoanReminderDto loanReminderDto, @MappingTarget LoanReminder loanReminder);
}
