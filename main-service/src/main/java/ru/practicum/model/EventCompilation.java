package ru.practicum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "event_compilation")
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
