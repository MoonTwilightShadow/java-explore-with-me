package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StatsServiceIntegrationTest {
    @Autowired
    private StatsService service;

    @Test
    public void statsServerAppTest() {
        EndpointHit hit = new EndpointHit("app", "url", "192.168.100.100", "2022-09-06 11:00:23");

        service.save(hit);

        List<ViewStats> stats = service.getStats("2022-01-01 11:00:00", "2023-01-01 11:00:00", null, false);
        assertEquals(1, stats.size());

        ViewStats savedHit = stats.get(0);

        assertEquals(hit.getApp(), savedHit.getApp());
        assertEquals(hit.getUri(), savedHit.getUri());
        assertEquals(1, savedHit.getHits());
    }
}
