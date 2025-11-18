package com.attendify.attendify_api.shared.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SanitizeValidator.class)
public @interface Sanitize {
    String message() default "Invalid characters detected in field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
