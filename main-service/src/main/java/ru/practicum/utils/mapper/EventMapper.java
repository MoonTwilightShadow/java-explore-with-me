package ru.practicum.utils.mapper;

import ru.practicum.dto.EventFullDTO;
import ru.practicum.dto.EventShortDTO;
import ru.practicum.dto.Location;
import ru.practicum.dto.NewEventDTO;
import ru.practicum.model.Event;
import ru.practicum.utils.Constants;

public class EventMapper {
    public static EventShortDTO mapToShort(Event event, Integer confirmedRequests) {
        return new EventShortDTO(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoryMapper.mapToDto(event.getCategory()),
                event.getEventDate().format(Constants.DATE_TIME_FORMATTER),
                event.getPaid(),
                UserMapper.mapToShort(event.getInitiator()),
                confirmedRequests,
                event.getViews()
        );
    }

    public static EventFullDTO mapToFull(Event event, Integer confirmedRequests) {
        return new EventFullDTO(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getDescription(),
                CategoryMapper.mapToDto(event.getCategory()),
                UserMapper.mapToShort(event.getInitiator()),
                new Location(event.getLat(), event.getLon()),
                event.getPaid(),
                event.getCreatedOn(),
                event.getEventDate(),
                event.getPublishedOn(),
                event.getParticipentLimit(),
                confirmedRequests,
                event.getModeration(),
                event.getState(),
                event.getViews()
        );
    }

    public static Event mapToEvent(NewEventDTO newEvent) {
        return new Event(
                newEvent.getTitle(),
                newEvent.getAnnotation(),
                newEvent.getDescription(),
                newEvent.getEventDate(),
                newEvent.getPaid(),
                newEvent.getParticipantLimit(),
                newEvent.getRequestModeration(),
                newEvent.getLocation().getLat(),
                newEvent.getLocation().getLon()
        );
    }
}
