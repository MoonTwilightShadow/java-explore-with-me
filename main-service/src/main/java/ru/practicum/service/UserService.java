package ru.practicum.service;

import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers(List<Integer> ids, Integer from, Integer size);

    UserDTO createUser(NewUserRequest newUser);

    void deleteUser(Integer id);
}
