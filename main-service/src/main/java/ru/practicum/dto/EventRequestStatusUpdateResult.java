package ru.practicum.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDTO> confirmedRequests;
    private List<ParticipationRequestDTO> rejectedRequests;
}
