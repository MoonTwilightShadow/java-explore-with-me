package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Rating;
import ru.practicum.model.enums.LikeType;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findRatingByEventIdAndUserId(Integer eventId, Integer userId);

    Integer countAllByEventIdAndLikeTypeIs(Integer eventId, LikeType likeType);

}
