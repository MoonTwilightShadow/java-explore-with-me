package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.model.StatView;
import ru.practicum.repository.StatsRepository;
import ru.practicum.utils.StatsMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatsRepository repository;

    @Override
    public void save(EndpointHit hit) {
        log.info("save method service");
        repository.save(StatsMapper.mapToHit(hit));
    }

    @Override
    public List<ViewStats> getStats(String startString, String endString, List<String> uris, Boolean unique) {
        log.info("getStats method service");

        LocalDateTime start = LocalDateTime.parse(startString, formatter);
        LocalDateTime end = LocalDateTime.parse(endString, formatter);

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("start parameter should be before end");
        }

        List<StatView> stats;

        if (uris == null) {
            if (unique) {
                stats = repository.findAllByTimestampBetweenUniqueIp(start, end);
            } else {
                stats = repository.findAllByTimestampBetween(start, end);
            }
        } else {
            if (unique) {
                stats = repository.findAllByTimestampBetweenAndUriInUniqueIp(start, end, uris);
            } else {
                stats = repository.findAllByTimestampBetweenAndUriIn(start, end, uris);
            }
        }

        return stats.stream().map(StatsMapper::mapToResponse).collect(Collectors.toList());
    }
}
