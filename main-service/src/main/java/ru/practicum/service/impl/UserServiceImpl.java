package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDTO;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;
import ru.practicum.utils.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getUsers(List<Integer> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        return Objects.isNull(ids) ? new ArrayList<>() : userRepository.findUserByIdIn(ids, page).stream()
                .map(UserMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO createUser(NewUserRequest newUser) {
        return UserMapper.mapToDTO(userRepository.save(UserMapper.mapFromDTO(newUser)));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id={} was not found", id)));

        userRepository.deleteById(id);
    }
}
