package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.StatView;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Integer> {
    @Query(value = "select hit.app as app, hit.uri as uri, count(distinct(hit.ip)) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "and hit.uri IN (?3) " +
            "group by hit.uri, hit.app " +
            "order by count(distinct(hit.ip)) desc")
    List<StatView> findAllByTimestampBetweenAndUriInUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select hit.app as app, hit.uri as uri, count(hit.ip) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "and hit.uri IN (?3) " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<StatView> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select hit.app as app, hit.uri as uri, count(distinct(hit.ip)) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(distinct(hit.ip)) desc")
    List<StatView> findAllByTimestampBetweenUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "select hit.app as app, hit.uri as uri, count(hit.ip) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<StatView> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
