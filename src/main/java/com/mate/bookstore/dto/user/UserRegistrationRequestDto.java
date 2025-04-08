package com.mate.bookstore.dto.user;

import com.mate.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "password", second = "repeatPassword",
        message = "Password and Repeat Password must match")
public record UserRegistrationRequestDto(
        @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required") String email,
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "Repeat password is required") String repeatPassword,
        @NotBlank(message = "Firstname is required") String firstName,
        @NotBlank(message = "Lastname is required") String lastName,
        String shippingAddress) {
}
