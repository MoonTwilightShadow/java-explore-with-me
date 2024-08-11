package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EventShortDTO;
import ru.practicum.model.Event;
import ru.practicum.model.enums.State;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.EventService;
import ru.practicum.utils.Constants;
import ru.practicum.utils.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<EventShortDTO> getEvents(
            String text,
            List<Integer> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size,
            Boolean onlyAvailable,
            String sort,
            String ip) {
        statsClient.saveHit("ewm-main-service", "/events", ip, LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));

        LocalDateTime start = rangeStart == null ? null : LocalDateTime.parse(rangeStart, Constants.DATE_TIME_FORMATTER);
        LocalDateTime end = rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, Constants.DATE_TIME_FORMATTER);

        List<Event> events = eventRepository.getEvents(text, categories, paid, start, end, from, size);

        Map<Integer, Integer> eventRequests = requestRepository.findAllByEventIdInAndStatus(
                        events.stream().map(Event::getId).collect(toList()), State.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEvent().getId(),
                        summingInt(e -> 1)
                ));

        if (Objects.nonNull(onlyAvailable) && onlyAvailable) {
            events = events.stream()
                    .filter(e -> (e.getParticipentLimit() >= eventRequests.get(e.getId()) ||
                            e.getParticipentLimit() == Constants.DEFAULT_PARTICIPANT_LIMIT))
                    .collect(toList());
        }

        List<EventShortDTO> eventShorts = events.stream().map(event -> {

            return EventMapper.mapToShort(
                    event,
                    eventRequests.getOrDefault(event.getId(), 0),
                    statsClient.getStats(
                            event.getCreatedOn().format(Constants.DATE_TIME_FORMATTER),
                            LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER),
                            List.of("/event/" + event.getId()),
                            false)
            );
        })
        if (sort.equals("EVENT_DATE")) {
            return eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(toList());
        } else {
            return eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getViews)).collect(toList());
        }


        return null;
    }
}
