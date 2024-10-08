package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
