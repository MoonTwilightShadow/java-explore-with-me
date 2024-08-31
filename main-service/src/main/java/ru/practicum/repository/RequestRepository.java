package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;
import ru.practicum.model.enums.State;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByEventIdInAndStatus(List<Integer> ids, State status);

    List<Request> findAllByEventIdAndStatus(Integer id, State status);

    List<Request> findAllByEventIdIn(List<Integer> ids);

    Integer countAllByEventId(Integer id);
    List<Request> findAllByRequestorId(Integer id);
}
