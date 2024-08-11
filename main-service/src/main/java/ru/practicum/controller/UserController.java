package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDTO;
import ru.practicum.service.UserService;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    //Admin Enpoints
    @GetMapping("/admin/users")
    public UserDTO getUsers() {
        return null;
    }

    @PostMapping("/admin/users")
    public UserDTO createUser() {
        return null;
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {

    }
}
