package ru.practicum.service;


import ru.practicum.dto.EventShortDTO;

import java.util.List;

public interface EventService {

    List<EventShortDTO> getEvents(String text,
                                  List<Integer> categories,
                                  Boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  Integer from,
                                  Integer size,
                                  Boolean onlyAvailable,
                                  String sort,
                                  String ip);
}
