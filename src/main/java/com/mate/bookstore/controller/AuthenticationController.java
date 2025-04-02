package com.mate.bookstore.controller;

import com.mate.bookstore.dto.UserDto;
import com.mate.bookstore.dto.UserLoginRequestDto;
import com.mate.bookstore.dto.UserLoginResponseDto;
import com.mate.bookstore.dto.UserRegistrationRequestDto;
import com.mate.bookstore.security.AuthenticationService;
import com.mate.bookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody UserRegistrationRequestDto userDto) {
        return userService.register(userDto);
    }

}
