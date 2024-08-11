package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
