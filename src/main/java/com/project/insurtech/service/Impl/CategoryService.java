package com.project.insurtech.service.Impl;

import com.project.insurtech.dtos.CategoryDTO;
import com.project.insurtech.entities.Category;
import com.project.insurtech.entities.User;
import com.project.insurtech.enums.IsDeletedEnum;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.components.mappers.CategoryMapper;
import com.project.insurtech.repositories.ICategoryRepository;
import com.project.insurtech.repositories.IUserRepository;
import com.project.insurtech.responses.Category.CategoryResponse;
import com.project.insurtech.service.ICategoryService;
import com.project.insurtech.specifications.CategorySpecification;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final IUserRepository userRepository;

    @Override
    public List<CategoryResponse> getCategories() {
        logger.info("Fetching categories from database");
        List<Category> categories = categoryRepository.findAllByIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
        return categories.stream()
                .map(categoryMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public Category createCategory(CategoryDTO categoryDTO, Long providerId) throws DataNotFoundException {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new DataNotFoundException("Provider not found with id " + providerId));
        Category category = categoryMapper.fromDTOtoEntity(categoryDTO);
        category.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
        category.setProvider(provider);
        category.setCreatedBy(providerId);
        category.setModifiedBy(providerId);
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
