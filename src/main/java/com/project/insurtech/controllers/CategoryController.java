package com.project.insurtech.controllers;

import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.components.helpers.ValidationHelper;
import com.project.insurtech.dtos.CategoryDTO;
import com.project.insurtech.entities.Category;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Category.CategoryResponse;
import com.project.insurtech.responses.User.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import com.project.insurtech.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private final RequestHelper requestUtil;
    private final ValidationHelper validationHelper;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getCategoryList(HttpServletRequest request) {
        List<CategoryResponse> categoryList = categoryService.getCategories();

        return ResponseEntity.ok(ResponseObject.builder()
                .message("Categories retrieved successfully")
                .status(HttpStatus.OK)
                .data(categoryList)
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result,
            HttpServletRequest request
    ) {
        if (result.hasErrors()) {
            return validationHelper.handleValidationErrors(result);
        }

        try {
            categoryService.createCategory(categoryDTO, requestUtil.getUserId(request));
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Category created successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            logger.error("Data not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                    .message("Data not found: " + e.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } catch (Exception e) {
            logger.error("Failed to create category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to create category due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Long id,
                                                         @Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Category updated successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                    .message("Category not found with id " + id)
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to update category due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Category deleted successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Failed to delete category")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

}
