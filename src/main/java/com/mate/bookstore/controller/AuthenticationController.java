package com.mate.bookstore.controller;

import com.mate.bookstore.dto.UserDto;
import com.mate.bookstore.dto.UserRegistrationRequestDto;
import com.mate.bookstore.service.AuthService;
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
    private final AuthService authService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    public UserDto register(@Valid @RequestBody UserRegistrationRequestDto userDto) {
        return authService.register(userDto);
    }
}
