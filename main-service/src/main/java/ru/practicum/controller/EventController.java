package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDTO;
import ru.practicum.dto.EventShortDTO;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
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
    public List<EventShortDTO> getEvent(@PathVariable(value = "id") Integer id) {
        return null;
    }

    //Private Endpoints
    @GetMapping("/users/{userId}/events")
    public List<EventShortDTO> getUserEvents() {
        return null;
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDTO createEvent() {
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    private EventFullDTO getUserEvent() {
        return null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    private EventFullDTO updateUserEvent() {
        return null;
    }

    //Admin Endpoints
    @GetMapping("/admin/events")
    public List<EventFullDTO> adminGetEvents() {
        return null;
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDTO adminUpdateEvent() {
        return null;
    }
}
