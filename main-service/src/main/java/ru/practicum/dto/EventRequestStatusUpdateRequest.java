package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.model.enums.State;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Integer> requestIds;
    @NotNull
    private State status;
}
