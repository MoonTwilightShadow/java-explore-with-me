package ru.practicum.exception.exceptions;

public class ParticipentsLimitExceedException extends RuntimeException {
    public ParticipentsLimitExceedException(String message) {
        super(message);
    }
}
