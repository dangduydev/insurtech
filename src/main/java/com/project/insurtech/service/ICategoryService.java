package com.project.insurtech.service;

import com.project.insurtech.dtos.CategoryDTO;
import com.project.insurtech.entities.Category;
import com.project.insurtech.exceptions.DataNotFoundException;

import java.util.List;

public interface ICategoryService {

    List<Category> getCategories();

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(Long id, CategoryDTO categoryDTO) throws DataNotFoundException;

    void deleteCategory(Long id) throws DataNotFoundException;
}
