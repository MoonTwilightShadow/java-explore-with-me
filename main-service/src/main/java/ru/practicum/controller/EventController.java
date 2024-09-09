package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDTO;
import ru.practicum.dto.EventShortDTO;
import ru.practicum.dto.NewEventDTO;
import ru.practicum.dto.UpdateEventRequest;
import ru.practicum.model.enums.State;
import ru.practicum.service.EventService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;

    //Public Endpoints
    @GetMapping("/events")
    public List<EventShortDTO> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Get events text={}, categories={}, paid={}, start={}, end={}, available={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, from, size, onlyAvailable, sort, request.getRemoteAddr());
    }

    @GetMapping("/events/{id}")
    public EventFullDTO getEvent(@PathVariable(value = "id") Integer id, HttpServletRequest request) {
        log.info("Get event with id={}. Ip={}, URI={}", id, request.getRemoteAddr(), request.getRequestURI());
        return eventService.getEvent(id, request.getRemoteAddr());
    }

    //Private Endpoints
    @GetMapping("/users/{userId}/events")
    public List<EventShortDTO> getUserEvents(
            @PathVariable(value = "userId") Integer userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get events with owner id={} from={} size={}", userId, from, size);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventFullDTO createEvent(
            @RequestBody @Valid NewEventDTO newEvent,
            @PathVariable Integer userId) {
        log.info("Post event with owner id={}, event={}", userId, newEvent);
        return eventService.createEvent(newEvent, userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    private EventFullDTO getUserEvent(
            @PathVariable(value = "userId") Integer userId,
            @PathVariable(value = "eventId") Integer eventId
    ) {
        log.info("Get event with id={} and user with id={}", eventId, userId);
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    private EventFullDTO updateUserEvent(
            @PathVariable Integer userId,
            @PathVariable Integer eventId,
            @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Patch event with id={} and user id={}, event={}", eventId, userId, updateEventRequest);
        return eventService.updateUserEvent(userId, eventId, updateEventRequest);
    }

    //Admin Endpoints
    @GetMapping("/admin/events")
    public List<EventFullDTO> adminGetEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get events with ids={}, states={}, categories={}, start={}, end={}, from={}, size={} by admin", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.adminGetEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDTO adminUpdateEvent(
            @PathVariable Integer eventId,
            @Valid @RequestBody UpdateEventRequest request) {
        log.info("Patch event with id={} event request={}", eventId, request);
        return eventService.adminUpdateEvent(eventId, request);
    }
}
