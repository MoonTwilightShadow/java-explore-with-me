package ru.practicum.exception.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateTimeValidator implements ConstraintValidator<EventDateTime, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        return eventDate == null || eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
