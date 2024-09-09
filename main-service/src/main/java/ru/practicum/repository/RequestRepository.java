package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;
import ru.practicum.model.enums.State;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Integer countAllByEventId(Integer id);

    Integer countAllByEventIdAndStatus(Integer id, State state);

    List<Request> findAllByRequesterId(Integer id);

    Optional<Request> findRequestByRequesterIdAndEventId(Integer userId, Integer eventId);

    List<Request> findRequestByEventIdAndEventInitiatorId(Integer eventId, Integer userId);

    List<Request> findAllByIdIn(List<Integer> ids);
}
