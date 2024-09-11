package ru.practicum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    @NotNull
    @Email(message = "{email incorrect}")
    @Size(min = 6, max = 254, message = "{email size should be from 6 to 254 letters}")
    private String email;
    @NotBlank
    private String name;
}
