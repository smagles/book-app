package com.mate.bookstore.controller.openapi;

import com.mate.bookstore.dto.user.UserDto;
import com.mate.bookstore.dto.user.UserLoginRequestDto;
import com.mate.bookstore.dto.user.UserLoginResponseDto;
import com.mate.bookstore.dto.user.UserRegistrationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication API",
        description = "Endpoints for user authentication and registration")
public interface AuthenticationApi {
    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    UserLoginResponseDto login(@RequestBody UserLoginRequestDto request);

    @Operation(summary = "Register new user",
            description = "Registers a new user and returns user details")
    @ApiResponse(responseCode = "201", description = "User registered successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "409", description = "User already exists")
    UserDto register(@RequestBody @Valid UserRegistrationRequestDto userDto);
}
