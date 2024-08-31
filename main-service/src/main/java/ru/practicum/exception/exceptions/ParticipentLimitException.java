package ru.practicum.exception.exceptions;

public class ParticipentLimitException extends RuntimeException {
    public ParticipentLimitException(String message) {
        super(message);
    }
}
