package ru.practicum.utils.mapper;

import ru.practicum.dto.EventShortDTO;
import ru.practicum.model.Event;
import ru.practicum.utils.Constants;

public class EventMapper {
    public static EventShortDTO mapToShort(Event event, Integer confirmedRequests, Integer views) {
        return new EventShortDTO(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoryMapper.mapToDto(event.getCategory()),
                event.getEventDate().format(Constants.DATE_TIME_FORMATTER),
                event.getPaid(),
                UserMapper.mapToShort(event.getInitiator()),
                confirmedRequests,
                views
        );
    }
}
