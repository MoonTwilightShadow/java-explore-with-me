package ru.practicum.exception.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventDateTimeValidator.class)
public @interface EventDateTime {
    String message() default "Start should be before than 2 hours from now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
