package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDTO {
    @NotBlank
    @Size(min = 1, max = 50, message = "{compilation title size should be from 1 to 50 letters}")
    private String title;
    private Boolean pinned;
    private List<Integer> events;
}
