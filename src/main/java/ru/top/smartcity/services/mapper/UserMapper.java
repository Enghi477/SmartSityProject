package ru.top.smartcity.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.top.smartcity.DTOs.UserDTO;
import ru.top.smartcity.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(User user);

    User toEntity(UserDTO userDto);
}