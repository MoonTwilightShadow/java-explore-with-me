package ru.practicum.utils.mapper;

import ru.practicum.dto.CompilationDTO;
import ru.practicum.dto.NewCompilationDTO;
import ru.practicum.model.Compilation;

public class CompilationsMapper {
    public static CompilationDTO mapToDTO(Compilation compilation) {
        return new CompilationDTO(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned()
        );
    }

    public static Compilation mapFromDTO(NewCompilationDTO newCompilation) {
        return new Compilation(
                newCompilation.getTitle(),
                newCompilation.getPinned()
        );
    }
}
