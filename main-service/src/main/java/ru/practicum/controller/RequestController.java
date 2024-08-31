package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDTO;
import ru.practicum.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    //Private Endpoint
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDTO> getRequestsUserForEvent(@PathVariable Integer userId,
                                                                 @PathVariable Integer eventId) {
        return  null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public List<EventRequestStatusUpdateResult> updateStateOfRequests(@PathVariable Integer userId,
                                                                      @PathVariable Integer eventId,
                                                                      @Valid @RequestBody EventRequestStatusUpdateRequest statusUpdate) {
        return null;
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDTO> getUserRequest(@PathVariable Integer userId) {
        return  null;
    }

    @PostMapping("/users/{userId}/requests")
    public ParticipationRequestDTO createRequest(@PathVariable Integer userId,
                                                 @RequestParam Integer eventId) {
        return null;
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDTO canceledRequest(@PathVariable Integer userId,
                                                   @PathVariable Integer requestId) {
        return null;
    }
}
