package ru.practicum.service;

import ru.practicum.dto.CompilationDTO;
import ru.practicum.dto.NewCompilationDTO;
import ru.practicum.dto.UpdateCompilationDTO;

import java.util.List;

public interface CompilationService {
    List<CompilationDTO> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDTO getCompilation(Integer id);

    CompilationDTO createCompilation(NewCompilationDTO newCompilation);

    CompilationDTO updateCompilation(Integer compId, UpdateCompilationDTO updateCompilationDTO);

    void deleteCompilation(Integer compId);
}
