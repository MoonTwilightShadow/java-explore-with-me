package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDTO;
import ru.practicum.dto.NewCompilationDTO;
import ru.practicum.dto.UpdateCompilationDTO;
import ru.practicum.service.CompilationService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    //Public endpoints
    @GetMapping("/compilations")
    public List<CompilationDTO> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDTO getCompilation(@PathVariable Integer compId) {
        return compilationService.getCompilation(compId);
    }

    //Admin Endpoints
    @PostMapping("/admin/compilations")
    public CompilationDTO createCompilation(@Valid @RequestBody NewCompilationDTO newCompilation) {
        return compilationService.createCompilation(newCompilation);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDTO updateCompilation(@PathVariable Integer compId, @Valid @RequestBody UpdateCompilationDTO updateCompilation) {
        return compilationService.updateCompilation(compId, updateCompilation);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        compilationService.deleteCompilation(compId);
    }
}
