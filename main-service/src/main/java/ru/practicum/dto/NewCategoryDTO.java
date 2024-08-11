package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDTO {
    @NotNull
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    private String name;
}
