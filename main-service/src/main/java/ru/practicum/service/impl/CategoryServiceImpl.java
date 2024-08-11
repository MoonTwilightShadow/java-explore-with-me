package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDTO;
import ru.practicum.dto.NewCategoryDTO;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.utils.mapper.CategoryMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getCategories(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return categoryRepository.findAll(page).map(CategoryMapper::mapToDto).getContent();
    }

    @Override
    public CategoryDTO getCategory(Integer id) {
        return CategoryMapper.mapToDto(findById(id));
    }

    @Override
    public CategoryDTO createCategory(NewCategoryDTO newCategory) {
        return CategoryMapper.mapToDto(categoryRepository.save(CategoryMapper.mapFromDto(newCategory)));
    }

    @Override
    public CategoryDTO updateCategory(NewCategoryDTO newCategoryDTO, Integer id) {
        Category category = findById(id);
        category.setName(newCategoryDTO.getName());

        return CategoryMapper.mapToDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    private Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", id)));
    }
}
