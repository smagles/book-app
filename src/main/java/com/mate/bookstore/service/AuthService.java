package com.mate.bookstore.service;

import com.mate.bookstore.dto.UserDto;
import com.mate.bookstore.dto.UserRegistrationRequestDto;

public interface AuthService {
    UserDto register(UserRegistrationRequestDto userDto);
}
