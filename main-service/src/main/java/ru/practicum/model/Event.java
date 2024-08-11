package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.enums.State;

import javax.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private State state;
    private Double lat;
    private Double lon;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
}
