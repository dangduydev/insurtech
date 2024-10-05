package com.project.insurtech.service;

import com.project.insurtech.dtos.CategoryDTO;
import com.project.insurtech.entities.Category;
import com.project.insurtech.enums.IsDeletedEnum;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.components.mappers.CategoryMapper;
import com.project.insurtech.repositories.ICategoryRepository;
import com.project.insurtech.specifications.CategorySpecification;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAllByIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.fromDTOtoEntity(categoryDTO);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryDTO categoryDTO) throws DataNotFoundException {
        Specification<Category> spec = Specification
                .where(CategorySpecification.hasId(id))
                .and(CategorySpecification.isNotDeleted(IsDeletedEnum.NOT_DELETED));
        Optional<Category> optionalCategory = categoryRepository.findOne(spec);
        Category category = optionalCategory.orElseThrow(() -> new DataNotFoundException("Category not found with id " + id));
        categoryMapper.updateEntityFromDto(categoryDTO, category);
        return categoryRepository.save(category);
    }


    @Override
    public void deleteCategory(Long id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found with id " + id));
        category.setIsDeleted(IsDeletedEnum.DELETED.getValue());
        categoryRepository.save(category);
    }

}
