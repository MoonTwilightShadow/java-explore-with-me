package ru.practicum.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import ru.practicum.dto.CategoryDTO;
import ru.practicum.dto.NewCategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getCategories(Integer from, Integer size);

    CategoryDTO getCategory(Integer id);

    CategoryDTO createCategory(NewCategoryDTO newCategory);

    CategoryDTO updateCategory(NewCategoryDTO newCategoryDTO, Integer catId);

    void deleteCategory(Integer id);
}
