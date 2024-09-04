package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDTO;
import ru.practicum.dto.NewCompilationDTO;
import ru.practicum.dto.UpdateCompilationDTO;
import ru.practicum.service.CompilationService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;

    //Public endpoints
    @GetMapping("/compilations")
    public List<CompilationDTO> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Get compilations from={}, size={}, pinned={}", from, size, pinned);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDTO getCompilation(@PathVariable Integer compId) {
        log.info("Get compilation with id={}", compId);
        return compilationService.getCompilation(compId);
    }

    //Admin Endpoints
    @PostMapping("/admin/compilations")
    public CompilationDTO createCompilation(@Valid @RequestBody NewCompilationDTO newCompilation) {
        log.info("Post compilation={}", newCompilation);
        return compilationService.createCompilation(newCompilation);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDTO updateCompilation(@PathVariable Integer compId, @Valid @RequestBody UpdateCompilationDTO updateCompilation) {
        log.info("Patch compilation with id={} compilation={}", compId, updateCompilation);
        return compilationService.updateCompilation(compId, updateCompilation);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("Delete compilation with id={}", compId);
        compilationService.deleteCompilation(compId);
    }
}
