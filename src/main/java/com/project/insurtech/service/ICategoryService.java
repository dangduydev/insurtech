package com.project.insurtech.service;

import com.project.insurtech.dtos.CategoryDTO;
import com.project.insurtech.entities.Category;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Category.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoryService {

    List<CategoryResponse> getCategories(Long providerId);

    Category createCategory(CategoryDTO categoryDTO, Long providerId) throws DataNotFoundException;

    Category updateCategory(Long id, CategoryDTO categoryDTO) throws DataNotFoundException;

    void deleteCategory(Long id) throws DataNotFoundException;
}
