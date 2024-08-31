package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.EventCompilation;
import ru.practicum.model.Compilation;
import ru.practicum.model.EventCompilation;

import java.util.List;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Integer> {
    void deleteAllByCompilation(Compilation compilation);

    List<EventCompilation> findAllByCompilationId(Integer id);

    List<EventCompilation> findAllByCompilationIn(List<Compilation> compilations);
}
