package ru.practicum.repository;

import ru.practicum.model.Event;
import ru.practicum.model.enums.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventCriteriaSearch {
    List<Event> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime start, LocalDateTime end, Integer from, Integer size);

    List<Event> adminGetEvents(List<Integer> userIds, List<Integer> categories, List<State> states, LocalDateTime start, LocalDateTime end, Integer from, Integer size);
}
