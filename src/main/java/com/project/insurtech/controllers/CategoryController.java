package com.project.insurtech.controllers;

import com.project.insurtech.dtos.CategoryDTO;
import com.project.insurtech.entities.Category;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Category.CategoryResponse;
import com.project.insurtech.responses.User.ResponseObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import com.project.insurtech.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getCategoryList() {
        List<Category> categoryList = categoryService.getCategories();

        return ResponseEntity.ok(ResponseObject.builder()
                .message("Categories retrieved successfully")
                .status(HttpStatus.OK)
                .data(categoryList)
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                         BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Validation failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Category created successfully")
                .status(HttpStatus.OK)
                .build());
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
