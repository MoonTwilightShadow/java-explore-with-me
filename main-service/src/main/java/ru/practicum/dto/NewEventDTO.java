package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exception.validation.EventDateTime;
import ru.practicum.model.Category;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDTO {
    @NotBlank
    @Size(min = 3, max = 120, message = "{title size should be from 20 to 7000 letters}")
    private String title;
    @NotBlank
    @Size(min = 20, max = 2000, message = "{annotation size should be from 20 to 2000 letters}")
    private String annotation;
    @NotBlank
    @Size(min = 20, max = 7000, message = "{description size should be from 20 to 7000 letters}")
    private String description;
    @NotNull
    @EventDateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Integer categoryId;
    @NotNull
    private Location location;
    private Boolean paid;
    private Integer participentLimit;
    private Boolean requestModeration;
}
