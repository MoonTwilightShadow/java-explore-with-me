package ru.practicum.utils.mapper;

import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDTO;
import ru.practicum.dto.UserShortDTO;
import ru.practicum.model.User;

public class UserMapper {
    public static UserShortDTO mapToShort(User user) {
        return new UserShortDTO(user.getId(), user.getName());
    }

    public static UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static User mapFromDTO(NewUserRequest newUser) {
        return new User(
                newUser.getName(),
                newUser.getEmail()
        );
    }
}
