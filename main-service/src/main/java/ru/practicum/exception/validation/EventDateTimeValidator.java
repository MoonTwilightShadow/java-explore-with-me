package ru.practicum.exception.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EventDateTimeValidator implements ConstraintValidator<EventDateTime, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        return eventDate == null || eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
