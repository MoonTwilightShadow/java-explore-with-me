package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDTO;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.exception.exceptions.ParticipentsLimitExceedException;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.RequestService;
import ru.practicum.utils.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDTO> getRequestsUserForEvent(Integer userId, Integer eventId) {
        return null;
    }

    @Override
    public List<EventRequestStatusUpdateResult> updateStateOfRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest statusUpdate) {
        return null;
    }

    @Override
    public List<ParticipationRequestDTO> getUserRequest(Integer userId) {
        return requestRepository.findAllByRequestorId(userId).stream()
                .map(RequestMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDTO createRequest(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", userId)));

        Integer participents = requestRepository.findAllByEventIdAndStatus(eventId, State.CONFIRMED)
                .stream().mapToInt(e -> 1).sum();

        if (participents >= event.getParticipentLimit()) {
            throw new ParticipentsLimitExceedException("The limit of participents has been exceeded.");
        }

        Request request = new Request();
        request.setRequestor(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        request.setStatus(event.getModeration() ? State.PENDING : State.CONFIRMED);

        return RequestMapper.mapToDTO(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDTO canceledRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id={} was not found", requestId)));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));

        if (userId != request.getRequestor().getId()) {
            throw new IllegalArgumentException("This request was sent by another user");
        }

        request.setStatus(State.CANCELED);
        return RequestMapper.mapToDTO(requestRepository.save(request));
    }
}
