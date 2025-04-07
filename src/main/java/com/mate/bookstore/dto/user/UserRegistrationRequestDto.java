package com.mate.bookstore.dto.user;

import com.mate.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@FieldMatch(first = "password", second = "repeatPassword",
        message = "Password and Repeat Password must match")
public record UserRegistrationRequestDto(
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "Repeat password is required") String repeatPassword,
        @NotBlank(message = "Firstname is required") String firstName,
        @NotBlank(message = "Lastname is required") String lastName,
        String shippingAddress) {
}
