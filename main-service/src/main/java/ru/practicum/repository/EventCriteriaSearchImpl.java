package ru.practicum.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.enums.State;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class EventCriteriaSearchImpl implements EventCriteriaSearch {
    private EntityManager entityManager;

    @Override
    public List<Event> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime start, LocalDateTime end, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = cb.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(eventRoot.get("state"), State.PUBLISHED));

        if (Objects.nonNull(text) && !text.isBlank()) {
            predicates.add(
                    cb.or(
                            cb.like(cb.upper(eventRoot.get("annotation")), "%" + text.toUpperCase() + "%"),
                            cb.like(cb.upper(eventRoot.get("description")), "%" + text.toUpperCase() + "%")
                    ));
        }

        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            predicates.add(eventRoot.get("category").get("id").in(categories));
        }

        if (Objects.nonNull(paid)) {
            predicates.add(cb.equal(eventRoot.get("paid"), paid));
        }

        predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("eventDate"), Objects.requireNonNullElseGet(start, LocalDateTime::now)));

        if (end != null) {
            predicates.add(cb.lessThanOrEqualTo(eventRoot.get("eventDate"), end));
        }

        return entityManager.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[0])))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Event> adminGetEvents(List<Integer> userIds, List<Integer> categories, List<State> states, LocalDateTime start, LocalDateTime end, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = cb.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(userIds) && !userIds.isEmpty()) {
            predicates.add(eventRoot.get("initiator").get("id").in(userIds));
        }

        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            predicates.add(eventRoot.get("category").get("id").in(categories));
        }

        if (Objects.nonNull(states) && !states.isEmpty()) {
            predicates.add(eventRoot.get("state").in(states));
        }

        if (Objects.nonNull(start)) {
            predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("eventDate"), start));
        }

        if (Objects.nonNull(end)) {
            predicates.add(cb.lessThanOrEqualTo(eventRoot.get("eventDate"), end));
        }

        return entityManager.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[0])))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }
}
