package ru.practicum.utils.mapper;

import ru.practicum.dto.ParticipationRequestDTO;
import ru.practicum.model.Request;

public class RequestMapper {
    public static ParticipationRequestDTO mapToDTO(Request request) {
        return new ParticipationRequestDTO(
                request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getCreated(),
                request.getStatus()
        );
    }
}
