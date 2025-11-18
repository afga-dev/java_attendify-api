package com.attendify.attendify_api.shared.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SanitizeValidator implements ConstraintValidator<Sanitize, String> {
    private static final Pattern FORBIDDEN_PATTERN = Pattern.compile("[<>\"'%;(){}\\\\]");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        String trimmed = value.trim();

        return !FORBIDDEN_PATTERN.matcher(trimmed).find();
    }
}
