package org.hcmus.premiere.util.mapper;

import org.hcmus.premiere.model.dto.UserDto;
import org.hcmus.premiere.model.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toEntity(UserDto userDto);

  @InheritInverseConfiguration(name = "toEntity")
  UserDto toDto(User user);
}
