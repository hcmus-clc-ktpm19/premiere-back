package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.CreditCardDto;
import org.hcmus.premiere.model.entity.CreditCard;
import org.hcmus.premiere.service.UserService;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CreditCardMapper {

  @Autowired
  protected UserService userService;

  @Mapping(target = "user", expression = "java(userService.findUserById(creditCardDto.getUserId()))")
  public abstract CreditCard toEntity(CreditCardDto creditCardDto);

  @InheritInverseConfiguration(name = "toEntity")
  @Mapping(target = "userId", source = "user.id")
  public abstract CreditCardDto toDto(CreditCard creditCard);

  @InheritConfiguration(name = "toDto")
  @Mapping(target = "balance", ignore = true)
  public abstract CreditCardDto toDtoIgnoreBalance(CreditCard creditCard);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract CreditCard partialUpdate(
      CreditCardDto creditCardDto, @MappingTarget CreditCard creditCard);
}
