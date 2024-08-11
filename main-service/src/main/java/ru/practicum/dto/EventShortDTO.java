package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDTO {
    private Integer id;
    private String title;
    private String annotation;
    private CategoryDTO category;
    private String eventDate;
    private Boolean paid;
    private UserShortDTO initiator;
    private Integer confirmedRequests;
    private Integer views;
}
