package com.project.insurtech.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.insurtech.components.helpers.PageHelper;
import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.components.helpers.ValidationHelper;
import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.PageResponse;
import com.project.insurtech.responses.Product.ProductListResponse;
import com.project.insurtech.responses.Product.ProductResponse;
import com.project.insurtech.responses.Provider.ProviderProductResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.IProductService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;
    private final ValidationHelper validationHelper;
    private final RequestHelper requestHelper;
    private final PageHelper pageHelper;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(HttpServletRequest request) {
        return ResponseEntity.ok(productService.getAllProductsByProviderId(requestHelper.getUserId(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        try {
            ProductResponse productResponse = productService.getProductById(id);
            return ResponseEntity.ok(productResponse);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result,
            HttpServletRequest request
    ) {
        if (result.hasErrors()) {
            validationHelper.handleValidationErrors(result);
        }
        try {
            productService.createProduct(requestHelper.getUserId(request), productDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Product created successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            // Log the error message for debugging purposes
            logger.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(ResponseObject.builder()
                    .message("Failed to create product")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, productDTO));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
        }
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<ProductListResponse>> userGetProducts(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "providerId", required = false) Long providerId,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            Pageable pageable
    ) {
        try {
            Pageable pageableWithSort = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)
            );
            Page<ProductListResponse> productResponses = productService.getFilteredProducts(categoryId, providerId, gender, pageableWithSort);
            PageResponse<ProductListResponse> response = pageHelper.toPagedResponse(productResponses);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handles any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count-by-provider")
    public ResponseEntity<List<ProviderProductResponse>> getProductCountByProvider() {
        List<ProviderProductResponse> result = productService.getProductCountByProvider();
        return ResponseEntity.ok(result);
    }


}
