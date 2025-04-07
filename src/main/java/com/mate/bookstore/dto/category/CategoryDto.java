package com.mate.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CategoryDto {
    @NotNull(message = "ID is required")
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    private String description;

}
