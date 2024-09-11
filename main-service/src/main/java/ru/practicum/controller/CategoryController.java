package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDTO;
import ru.practicum.dto.NewCategoryDTO;
import ru.practicum.service.CategoryService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    //Public endpoints
    @GetMapping("/categories")
    public List<CategoryDTO> getCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get categories from={} size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDTO getCategory(@PathVariable Integer catId) {
        log.info("Get category with id={}", catId);
        return categoryService.getCategory(catId);
    }

    //Admin Endpoints
    @PostMapping("/admin/categories")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDTO createCategory(
            @Validated @RequestBody NewCategoryDTO newCategory) {
        log.info("Post category={}", newCategory);
        return categoryService.createCategory(newCategory);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDTO updateCategory(
            @Validated @RequestBody NewCategoryDTO newCategory,
            @PathVariable Integer catId) {
        log.info("Patch category with id={}, category={}", catId, newCategory);
        return categoryService.updateCategory(newCategory, catId);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("Delete category with id={}", catId);
        categoryService.deleteCategory(catId);
    }
}
