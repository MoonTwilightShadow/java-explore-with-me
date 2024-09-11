package ru.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "event_compilations")
public class EventCompilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Compilation compilation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    public EventCompilation(Compilation compilation, Event event) {
        this.compilation = compilation;
        this.event = event;
    }
}
