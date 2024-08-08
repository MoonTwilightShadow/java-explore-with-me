package ru.practicum.service;

import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;

import java.util.List;

public interface StatsService {
    void save(EndpointHit hit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
