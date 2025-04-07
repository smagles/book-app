package com.mate.bookstore.service.user;

import com.mate.bookstore.dto.user.UserDto;
import com.mate.bookstore.dto.user.UserRegistrationRequestDto;

public interface UserService {
    UserDto register(UserRegistrationRequestDto userDto);
}
