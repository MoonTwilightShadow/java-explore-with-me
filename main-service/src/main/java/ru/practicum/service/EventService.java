package ru.practicum.service;


import ru.practicum.dto.EventFullDTO;
import ru.practicum.dto.EventShortDTO;
import ru.practicum.dto.NewEventDTO;
import ru.practicum.dto.UpdateEventRequest;
import ru.practicum.model.enums.State;

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

    EventFullDTO getEvent(Integer id, String ip);

    List<EventShortDTO> getUserEvents(Integer userId, Integer from, Integer size);

    EventFullDTO createEvent(NewEventDTO newEvent, Integer userId);

    EventFullDTO getUserEvent(Integer userId, Integer eventId);

    EventFullDTO updateUserEvent(Integer userId, Integer eventId, UpdateEventRequest update);

    EventShortDTO addLike(Integer eventId, Integer userId);

    EventShortDTO addDislike(Integer eventId, Integer userId);

    List<EventFullDTO> adminGetEvents(List<Integer> users,
                                      List<State> states,
                                      List<Integer> categories,
                                      String rangeStart,
                                      String rangeEnd,
                                      Integer from,
                                      Integer size);

    EventFullDTO adminUpdateEvent(Integer eventId, UpdateEventRequest update);
}
