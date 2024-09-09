package ru.practicum.service;

import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDTO;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDTO> getRequestsUserForEvent(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateStateOfRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest statusUpdate);

    List<ParticipationRequestDTO> getUserRequest(Integer userId);

    ParticipationRequestDTO createRequest(Integer userId, Integer eventId);

    ParticipationRequestDTO canceledRequest(Integer userId, Integer requestId);
}
