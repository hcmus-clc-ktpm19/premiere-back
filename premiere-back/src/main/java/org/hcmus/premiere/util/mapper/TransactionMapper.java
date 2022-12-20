package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.TransactionDto;
import org.hcmus.premiere.model.entity.Transaction;
import org.hcmus.premiere.service.TransactionService;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

  @Autowired
  protected TransactionService transactionService;

  public abstract Transaction toEntity(TransactionDto transactionDto);

  @InheritInverseConfiguration(name = "toEntity")
  public abstract TransactionDto toDto(
      Transaction transaction);

  @InheritConfiguration(name = "toEntity")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract Transaction partialUpdate(
      TransactionDto transactionDto, @MappingTarget Transaction transaction);
}
