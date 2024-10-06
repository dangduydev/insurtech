package com.project.insurtech.controllers;

import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Product.ProductListResponse;
import com.project.insurtech.responses.Product.ProductResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseObject> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Validation failed: " + String.join(", ", errorMessages))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        try {
            productService.createProduct(productDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Product created successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(ResponseObject.builder()
                    .message("Failed to create product: " + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long providerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "desc") String sort) {
        try {
            page = Math.max(0, page - 1); // Ensure page is not negative

            PageRequest pageRequest;
            if (Objects.equals(sort, "asc")) {
                pageRequest = PageRequest.of(
                        page, size,
                        Sort.by("id").ascending()
                );
            } else {
                pageRequest = PageRequest.of(
                        page, size,
                        Sort.by("id").descending()
                );
            }

            Pageable pageable = pageRequest;
            Page<ProductResponse> products = productService.getAllProducts(name, categoryId, providerId, status, pageable);
            Integer currentPage = products.getPageable().getPageNumber() + 1;
            return ResponseEntity.ok(ProductListResponse.builder()
                    .products(products.getContent())
                    .currentPage(currentPage)
                    .pageSize(products.getPageable().getPageSize())
                    .totalPages(products.getTotalPages())
                    .totalElements(products.getTotalElements())
                    .build());
        } catch (Exception e) {
            logger.error("Error getting products: {}", e.getMessage(), e); //Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateProduct(@Valid @PathVariable Long id,
                                                        @RequestBody ProductDTO productDTO) {
        try {
            productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Product updated successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Product deleted successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
