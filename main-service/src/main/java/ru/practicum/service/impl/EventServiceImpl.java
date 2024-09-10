package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EventFullDTO;
import ru.practicum.dto.EventShortDTO;
import ru.practicum.dto.NewEventDTO;
import ru.practicum.dto.UpdateEventRequest;
import ru.practicum.exception.exceptions.ConflictException;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.StateAction;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.utils.Constants;
import ru.practicum.utils.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

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

        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start parameter of search should be before than end");
            }
        }

        List<EventShortDTO> events = eventRepository.getEvents(text, categories, paid, start, end, from, size).stream()
                .filter(event -> Objects.isNull(onlyAvailable) || !onlyAvailable ||
                        (event.getParticipentLimit() >= requestRepository.countAllByEventId(event.getId()) ||
                                event.getParticipentLimit() == Constants.DEFAULT_PARTICIPANT_LIMIT))
                .map(event -> EventMapper.mapToShort(event, requestRepository.countAllByEventId(event.getId())))
                .toList();

        if (Objects.nonNull(sort) && sort.equals("EVENT_DATE")) {
            return events.stream().sorted(Comparator.comparing(EventShortDTO::getEventDate)).collect(toList());
        } else {
            return events.stream().sorted(Comparator.comparing(EventShortDTO::getViews)).collect(toList());
        }
    }

    @Override
    public EventFullDTO getEvent(Integer id, String ip) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", id)));

        if (!event.getState().equals(State.PUBLISHED))
            throw new NotFoundException(String.format("Event with id={} was not published", id));

        event.setViews(event.getViews() + 1);
        statsClient.saveHit("ewm-main-service", "/events/" + id, ip, LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));

        return EventMapper.mapToFull(event, requestRepository.countAllByEventId(id));
    }

    @Override
    public List<EventShortDTO> getUserEvents(Integer userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(event -> EventMapper.mapToShort(event, requestRepository.countAllByEventId(event.getId())))
                .collect(toList());
    }

    @Override
    public EventFullDTO createEvent(NewEventDTO newEvent, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));

        Category category = categoryRepository.findById(newEvent.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id={} was not found", newEvent.getCategory())));

        Event event = EventMapper.mapToEvent(newEvent);
        event.setInitiator(user);
        event.setCategory(category);

        if (Objects.isNull(newEvent.getPaid())) {
            event.setPaid(Constants.DEFAULT_PAID);
        }
        if (Objects.isNull(newEvent.getParticipantLimit())) {
            event.setParticipentLimit(Constants.DEFAULT_PARTICIPANT_LIMIT);
        }
        if (Objects.isNull(newEvent.getRequestModeration())) {
            event.setModeration(Constants.DEFAULT_REQUEST_MODERATION);
        }

        return EventMapper.mapToFull(eventRepository.save(event), 0);
    }

    @Override
    public EventFullDTO getUserEvent(Integer userId, Integer eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", eventId)));

        return EventMapper.mapToFull(event, requestRepository.countAllByEventId(eventId));
    }

    @Override
    public EventFullDTO updateUserEvent(Integer userId, Integer eventId, UpdateEventRequest update) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", eventId)));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("This event has already been published");
        }

        event = updateFields(event, update);

        if (Objects.nonNull(update.getStateAction())) {
            if (update.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else if (update.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            } else {
                throw new IllegalArgumentException(String.format("Unknown event state: %s", event.getState()));
            }
        }

        return EventMapper.mapToFull(eventRepository.save(event), requestRepository.countAllByEventId(eventId));
    }

    @Override
    public List<EventFullDTO> adminGetEvents(
            List<Integer> users,
            List<State> states,
            List<Integer> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size) {
        LocalDateTime start = rangeStart == null ? null : LocalDateTime.parse(rangeStart, Constants.DATE_TIME_FORMATTER);
        LocalDateTime end = rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, Constants.DATE_TIME_FORMATTER);

        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start parameter of search should be before than end");
            }
        }

        List<Event> events = eventRepository.adminGetEvents(
                users,
                categories,
                states,
                start,
                end,
                from,
                size);

        return events.stream()
                .map(e -> EventMapper.mapToFull(e, requestRepository.countAllByEventId(e.getId())))
                .collect(toList());
    }

    @Override
    public EventFullDTO adminUpdateEvent(Integer eventId, UpdateEventRequest update) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", eventId)));

        event = updateFields(event, update);

        if (Objects.nonNull(update.getStateAction())) {
            if (update.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(State.PENDING)) {
                    throw new ConflictException(String.format("Cannot publish the event because it's not in the right state: %s", event.getState()));
                } else {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
            } else if (update.getStateAction().equals(StateAction.REJECT_EVENT)) {
                if (event.getState().equals(State.PUBLISHED)) {
                    throw new ConflictException(String.format("Cannot reject the event because it's not in the right state: %s", event.getState()));
                } else {
                    event.setState(State.CANCELED);
                }
            } else {
                throw new IllegalArgumentException(String.format("Unknown event state: %s", event.getState()));
            }
        }

        return EventMapper.mapToFull(eventRepository.save(event), requestRepository.countAllByEventId(eventId));
    }

    private Event updateFields(Event event, UpdateEventRequest update) {
        Integer newCat = update.getCategory();
        if (Objects.nonNull(newCat) && event.getCategory().getId().equals(newCat)) {
            Category category = categoryRepository.findById(newCat)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id={} was not found", newCat)));

            event.setCategory(category);
        }

        if (Objects.nonNull(update.getParticipantLimit()) && update.getParticipantLimit() >= 0) {
            Integer participent = requestRepository.countAllByEventId(event.getId());

            if (participent > update.getParticipantLimit()) {
                throw new ConflictException("There are more participants than the new limit");
            }

            event.setParticipentLimit(update.getParticipantLimit());
        }

        if (Objects.nonNull(update.getTitle())) {
            event.setTitle(update.getTitle());
        }
        if (Objects.nonNull(update.getDescription())) {
            event.setDescription(update.getDescription());
        }
        if (Objects.nonNull(update.getAnnotation())) {
            event.setAnnotation(update.getAnnotation());
        }
        if (Objects.nonNull(update.getEventDate())) {
            event.setEventDate(update.getEventDate());
        }
        if (Objects.nonNull(update.getLocation())) {
            event.setLat(update.getLocation().getLat());
            event.setLon(update.getLocation().getLon());
        }
        if (Objects.nonNull(update.getPaid())) {
            event.setPaid(update.getPaid());
        }
        if (Objects.nonNull(update.getRequestModeration())) {
            event.setModeration(update.getRequestModeration());
        }

        return event;
    }
}