package ru.practicum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewUserRequest {
    @NotBlank
    @Email(message = "{email incorrect}")
    @Size(min = 6, max = 254, message = "{email size should be from 6 to 254 letters}")
    private String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "{name size should be from 2 to 250 letters}")
    private String name;
}
