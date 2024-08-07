package ru.practicum.utils;

import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.model.Hit;
import ru.practicum.model.StatView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ViewStats mapToResponse(StatView view) {
        return new ViewStats(
                view.getApp(),
                view.getUri(),
                view.getHits()
        );
    }

    public static Hit mapToHit(EndpointHit hit) {
        return new Hit(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                LocalDateTime.parse(hit.getTimestamp(), formatter)
        );
    }
}
