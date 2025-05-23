package com.mate.bookstore.mapper;

import com.mate.bookstore.config.MapperConfig;
import com.mate.bookstore.dto.user.UserDto;
import com.mate.bookstore.dto.user.UserRegistrationRequestDto;
import com.mate.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    UserDto toDto(User user);
}
