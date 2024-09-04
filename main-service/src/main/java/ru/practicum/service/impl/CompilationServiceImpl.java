package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDTO;
import ru.practicum.dto.EventShortDTO;
import ru.practicum.dto.NewCompilationDTO;
import ru.practicum.dto.UpdateCompilationDTO;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.EventCompilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventCompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.CompilationService;
import ru.practicum.utils.mapper.CompilationsMapper;
import ru.practicum.utils.mapper.EventMapper;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDTO> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, page);
        } else {
            compilations = compilationRepository.findAll(page).getContent();
        }

        Map<Compilation, List<Event>> compilationEvents = eventCompilationRepository
                .findAllByCompilationIn(compilations)
                .stream()
                .collect(Collectors.groupingBy(
                                EventCompilation::getCompilation,
                                Collectors.mapping(EventCompilation::getEvent, toList())
                        )
                );

        List<CompilationDTO> compilationDtoList = new ArrayList<>();

        for (Compilation compilation : compilations) {
            CompilationDTO compilationDto = CompilationsMapper.mapToDTO(compilation);
            if (compilationEvents.containsKey(compilation)) {
                compilationDto.setEvents(compilationEvents.get(compilation).stream()
                        .map(e -> EventMapper.mapToShort(e, requestRepository.countAllByEventId(e.getId())))
                        .collect(toList()));
            } else {
                compilationDto.setEvents(Collections.emptyList());
            }
            compilationDtoList.add(compilationDto);
        }
        return compilationDtoList;
    }

    @Override
    public CompilationDTO getCompilation(Integer id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id={} was not found", id)));

        List<EventShortDTO> events = eventCompilationRepository.findAllByCompilationId(id).stream()
                .map(e -> EventMapper.mapToShort(e.getEvent(), requestRepository.countAllByEventId(e.getEvent().getId())))
                .collect(toList());

        CompilationDTO compilationDTO = CompilationsMapper.mapToDTO(compilation);
        compilationDTO.setEvents(events);

        return compilationDTO;
    }

    @Override
    public CompilationDTO createCompilation(NewCompilationDTO newCompilation) {
        if (Objects.isNull(newCompilation.getPinned())) {
            newCompilation.setPinned(false);
        }

        Compilation compilation = compilationRepository.save(CompilationsMapper.mapFromDTO(newCompilation));
        List<Event> events = new ArrayList<>();

        for (Integer eventId : newCompilation.getEvents()) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not published", eventId)));

            eventCompilationRepository.save(new EventCompilation(compilation, event));

            events.add(event);
        }

        CompilationDTO compilationDTO = CompilationsMapper.mapToDTO(compilation);
        compilationDTO.setEvents(events.stream()
                .map(e -> EventMapper.mapToShort(e, requestRepository.countAllByEventId(e.getId())))
                .collect(toList()));

        return compilationDTO;
    }

    @Override
    public CompilationDTO updateCompilation(Integer compId, UpdateCompilationDTO updateCompilation) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id={} was not found", compId)));;

        if (Objects.nonNull(updateCompilation.getTitle())) {
            compilation.setTitle(updateCompilation.getTitle());
        }
        if (Objects.nonNull(updateCompilation.getPinned())) {
            compilation.setPinned(updateCompilation.getPinned());
        }

        List<Event> events = new ArrayList<>();
        if (Objects.nonNull(updateCompilation.getEvents())) {
            eventCompilationRepository.deleteAllByCompilation(compilation);

            for (Integer eventId : updateCompilation.getEvents()) {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException(String.format("Event with id={} was not published", eventId)));

                eventCompilationRepository.save(new EventCompilation(compilation, event));

                events.add(event);
            }
        } else {
            events.addAll(eventCompilationRepository.findAllByCompilationId(compId).stream()
                    .map(EventCompilation::getEvent)
                    .collect(toList()));
        }

        List<EventShortDTO> eventShortDTOS = events.stream()
                .map(e -> EventMapper.mapToShort(e, requestRepository.countAllByEventId(e.getId())))
                .collect(toList());

        CompilationDTO compilationDTO = CompilationsMapper.mapToDTO(compilation);
        compilationDTO.setEvents(eventShortDTOS);

        return compilationDTO;
    }

    @Override
    public void deleteCompilation(Integer compId) {
        compilationRepository.deleteById(compId);
    }
}
