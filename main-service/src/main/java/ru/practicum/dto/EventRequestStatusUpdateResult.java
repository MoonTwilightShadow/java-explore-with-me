package ru.practicum.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDTO> confirmedRequests;
    private List<ParticipationRequestDTO> rejectedRequests;
}
