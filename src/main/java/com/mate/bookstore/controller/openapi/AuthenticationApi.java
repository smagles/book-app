package com.mate.bookstore.controller.openapi;

import com.mate.bookstore.dto.user.UserDto;
import com.mate.bookstore.dto.user.UserLoginRequestDto;
import com.mate.bookstore.dto.user.UserLoginResponseDto;
import com.mate.bookstore.dto.user.UserRegistrationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication API",
        description = "Endpoints for user authentication and registration")
public interface AuthenticationApi {
    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @RequestBody(
            description = "User credentials",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginRequestDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "Standard login",
                                    value = """
                                            {
                                              "email": "john.doe@example.com",
                                              "password": "securePassword123"
                                            }
                                            """
                            )
                    }
            )
    )
    UserLoginResponseDto login(@RequestBody UserLoginRequestDto request);

    @Operation(summary = "Register new user",
            description = "Registers a new user and returns user details")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @RequestBody(
            description = "User registration data",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRegistrationRequestDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "Standard registration",
                                    value = """
                                            {
                                              "email": "john.doe@example.com",
                                              "password": "securePassword123",
                                              "repeatPassword": "securePassword123",
                                              "firstName": "John",
                                              "lastName": "Doe",
                                              "shippingAddress": "123 Main St, City, Country"
                                            }
                                            """
                            )
                    }
            )
    )
    UserDto register(@RequestBody @Valid UserRegistrationRequestDto userDto);
}
