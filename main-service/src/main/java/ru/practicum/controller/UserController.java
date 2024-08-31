package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDTO;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    //Admin Enpoints
    @GetMapping("/admin/users")
    public List<UserDTO> getUsers(@RequestParam(name = "ids", required = false) List<Integer> ids,
                            @RequestParam(defaultValue = "0") Integer from,
                            @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/admin/users")
    public UserDTO createUser(@Valid @RequestBody NewUserRequest newUser) {
        return userService.createUser(newUser);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestParam(name = "userId") Integer userId) {
        userService.deleteUser(userId);
    }
}
