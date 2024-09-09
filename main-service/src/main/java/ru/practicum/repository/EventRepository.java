package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, EventCriteriaSearch {
    List<Event> findAllByInitiatorId(Integer userId, PageRequest page);

    Optional<Event> findFirstByCategoryId(Integer id);
}
