package com.mate.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
    private List<String> acceptedValues;

    @Override
    public void initialize(EnumValidator annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && acceptedValues.contains(value.toUpperCase());
    }
}
