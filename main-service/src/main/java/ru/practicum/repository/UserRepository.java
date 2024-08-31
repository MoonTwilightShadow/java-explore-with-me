package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.User;

import java.awt.print.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUserByIdIn(List<Integer> ids, PageRequest page);
}
