package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.BankDto;
import org.hcmus.premiere.model.entity.Bank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankMapper {

  BankDto toDto(Bank bank);
}
