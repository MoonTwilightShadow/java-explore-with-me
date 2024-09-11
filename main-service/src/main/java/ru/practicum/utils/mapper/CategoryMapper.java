package ru.practicum.utils.mapper;

import ru.practicum.dto.CategoryDTO;
import ru.practicum.dto.NewCategoryDTO;
import ru.practicum.model.Category;

public class CategoryMapper {
    public static CategoryDTO mapToDto(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    public static Category mapFromDto(NewCategoryDTO categoryDTO) {
        return new Category(categoryDTO.getName());
    }
}
