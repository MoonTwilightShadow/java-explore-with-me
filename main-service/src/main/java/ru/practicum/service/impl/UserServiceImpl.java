package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDTO;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;
import ru.practicum.utils.mapper.UserMapper;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getUsers(List<Integer> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        if (Objects.isNull(ids)) {
            return userRepository.findAll(page).map(UserMapper::mapToDTO).getContent();
        }
        return userRepository.findUserByIdIn(ids, page).map(UserMapper::mapToDTO).getContent();
    }

    @Override
    public UserDTO createUser(NewUserRequest newUser) {
        User user = UserMapper.mapFromDTO(newUser);
        user = userRepository.save(user);
        return UserMapper.mapToDTO(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", id)));

        userRepository.deleteById(id);
    }
}
