package ru.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.enums.State;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String annotation;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participentLimit;
    @Column(name = "request_moderation")
    private Boolean moderation;
    @Enumerated(EnumType.STRING)
    private State state;
    private Double lat;
    private Double lon;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
    private Integer views;

    public Event(
            String title,
            String annotation,
            String description,
            LocalDateTime eventDate,
            Boolean paid,
            Integer participentLimit,
            Boolean requestModeration,
            Double lat,
            Double lon) {
        this.title = title;
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.createdOn = LocalDateTime.now();
        this.paid = paid;
        this.participentLimit = participentLimit;
        this.moderation = requestModeration;
        this.state = State.PENDING;
        this.lat = lat;
        this.lon = lon;
        this.views = 0;
    }
}
