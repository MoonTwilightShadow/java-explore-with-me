package ru.practicum.utils.mapper;

import ru.practicum.dto.UserShortDTO;
import ru.practicum.model.User;

public class UserMapper {
    public static UserShortDTO mapToShort(User user) {
        return new UserShortDTO(user.getId(), user.getName());
    }
}
