package ru.practicum;

import java.util.List;

public interface StatsClient {
    void saveHit(String app, String uri, String ip, String timestamp);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
