package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDTO;
import ru.practicum.exception.exceptions.ConflictException;
import ru.practicum.exception.exceptions.NotFoundException;
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
import java.util.Collections;
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
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", userId)));

        return requestRepository.findRequestByEventIdAndEventInitiatorId(eventId, userId).stream()
                .map(RequestMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateStateOfRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest statusUpdate) {
        if (!List.of(State.CONFIRMED, State.REJECTED).contains(statusUpdate.getStatus())) {
            throw new IllegalArgumentException("Wrong status to confirm or reject requests " + statusUpdate.getStatus());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", userId)));

        List<Request> requests = requestRepository.findAllByIdIn(statusUpdate.getRequestIds());

        if (requests.stream().anyMatch(r -> !r.getStatus().equals(State.PENDING))) {
            throw new ConflictException("All requests must have status PENDING");
        }

        if (State.REJECTED.equals(statusUpdate.getStatus())) {
            requests.forEach(r -> r.setStatus(State.REJECTED));
            requestRepository.saveAll(requests);
            return EventRequestStatusUpdateResult.builder()
                    .rejectedRequests(requests.stream()
                            .map(RequestMapper::mapToDTO)
                            .collect(Collectors.toList()))
                    .confirmedRequests(Collections.emptyList())
                    .build();
        }

        if (event.getParticipentLimit() == 0 || !event.getModeration()) {
            requests.forEach(r -> r.setStatus(State.CONFIRMED));
            requestRepository.saveAll(requests);

            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(requests.stream()
                            .map(RequestMapper::mapToDTO)
                            .collect(Collectors.toList()))
                    .rejectedRequests(Collections.emptyList())
                    .build();
        }

        Integer confirmedParticipantCount = requestRepository.countAllByEventIdAndStatus(eventId, State.CONFIRMED);
        int limit = event.getParticipentLimit() - confirmedParticipantCount;

        if (event.getParticipentLimit() != 0 && confirmedParticipantCount >= event.getParticipentLimit()) {
            throw new ConflictException("The participant limit has been reached");
        }

        if (limit >= requests.size()) {
            requests.forEach(r -> r.setStatus(State.CONFIRMED));
            requestRepository.saveAll(requests);
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(requests.stream().map(RequestMapper::mapToDTO).collect(Collectors.toList()))
                    .rejectedRequests(Collections.emptyList())
                    .build();
        } else {
            List<Request> confirm = requests.subList(0, limit);
            List<Request> reject = requests.subList(limit, requests.size());

            confirm.forEach(r -> r.setStatus(State.CONFIRMED));
            requestRepository.saveAll(confirm);

            reject.forEach(r -> r.setStatus(State.REJECTED));
            requestRepository.saveAll(reject);

            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(confirm.stream().map(RequestMapper::mapToDTO).collect(Collectors.toList()))
                    .rejectedRequests(reject.stream().map(RequestMapper::mapToDTO).collect(Collectors.toList()))
                    .build();
        }
    }

    @Override
    public List<ParticipationRequestDTO> getUserRequest(Integer userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDTO createRequest(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not found", userId)));

        if (requestRepository.findRequestByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("The request already exists");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("The required event not published");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The required event not published");
        }

        if (event.getParticipentLimit() != 0 && (event.getParticipentLimit() <= requestRepository.countAllByEventIdAndStatus(eventId, State.CONFIRMED))) {
            throw new ConflictException("The limit of participents has been exceeded.");
        }

        Request request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        request.setStatus(!event.getModeration() || event.getParticipentLimit() == 0 ? State.CONFIRMED : State.PENDING);

        return RequestMapper.mapToDTO(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDTO canceledRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id={} was not found", requestId)));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", userId)));

        if (!userId.equals(request.getRequester().getId())) {
            throw new IllegalArgumentException("This request was sent by another user");
        }

        request.setStatus(State.CANCELED);
        return RequestMapper.mapToDTO(requestRepository.save(request));
    }
}
