package com.example.user.mapper;

import com.example.user.domain.User;
import com.example.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * A mapper between User and UserDto. At compile time MapStruct will generate an implementation of this interface.
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto convertToDto(User user);

    User convertToUser(UserDto userDto);

}
