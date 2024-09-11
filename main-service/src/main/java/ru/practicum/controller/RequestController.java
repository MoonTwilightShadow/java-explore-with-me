package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDTO;
import ru.practicum.service.RequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    //Private Endpoint
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDTO> getRequestsUserForEvent(@PathVariable Integer userId,
                                                                 @PathVariable Integer eventId) {
        log.info("Get participation requests for user with id={} on event with id={}", userId, eventId);
        return requestService.getRequestsUserForEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStateOfRequests(@PathVariable Integer userId,
                                                                      @PathVariable Integer eventId,
                                                                      @Valid @RequestBody EventRequestStatusUpdateRequest statusUpdate) {
        log.info("Update event request status for event with id={} and user with id={} request={}", userId, eventId, statusUpdate);
        return requestService.updateStateOfRequests(userId, eventId, statusUpdate);
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDTO> getUserRequest(@PathVariable Integer userId) {
        log.info("Get requests for user with id={}", userId);
        return requestService.getUserRequest(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParticipationRequestDTO createRequest(@PathVariable Integer userId,
                                                 @RequestParam Integer eventId) {
        log.info("Post request for user with id={} on event with id={}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDTO canceledRequest(@PathVariable Integer userId,
                                                   @PathVariable Integer requestId) {
        log.info("Cancel request with id={} from user with id={} ", requestId, userId);
        return requestService.canceledRequest(userId, requestId);
    }
}
