package ru.practicum.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Integer DEFAULT_PARTICIPANT_LIMIT = 0;
    public static final Boolean DEFAULT_PAID = false;
    public static final Boolean DEFAULT_REQUEST_MODERATION = false;
}
