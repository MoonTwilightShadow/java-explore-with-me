package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Category;

import java.awt.print.Pageable;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
