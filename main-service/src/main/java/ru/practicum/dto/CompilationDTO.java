package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDTO {
    private Integer id;
    @NotBlank
    @Size(min = 1, max = 50, message = "{compilation title size should be from 1 to 50 letters}")
    private String title;
    @NotNull(message = "{compilation pinned should not be null}")
    private Boolean pinned;
    private List<EventShortDTO> events;

    public CompilationDTO(Integer id, String title, Boolean pinned) {
        this.id = id;
        this.title = title;
        this.pinned = pinned;
    }
}
