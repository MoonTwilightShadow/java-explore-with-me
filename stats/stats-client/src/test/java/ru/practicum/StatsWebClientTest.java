package ru.practicum;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsWebClientTest {
    private StatsWebClient client = new StatsWebClient("http://localhost:9090");;

    @Test
    public void saveHitTest() {
        EndpointHit hit = new EndpointHit("app", "url", "192.168.100.100", "2022-09-06 11:00:23");

        client.saveHit(hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());

        List<ViewStats> stats = client.getStats("2022-01-01 11:00:00", "2023-01-01 11:00:00", new ArrayList<>(), false);
        assertEquals(1, stats.size());

        ViewStats savedHit = stats.get(0);

        assertEquals(hit.getApp(), savedHit.getApp());
        assertEquals(hit.getUri(), savedHit.getUri());
        assertEquals(1, savedHit.getHits());
    }
}
