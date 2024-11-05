package com.project.insurtech.controllers;

import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.components.helpers.ValidationHelper;
import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.ProductResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.IProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;
    private final ValidationHelper validationHelper;
    private final RequestHelper requestHelper;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(HttpServletRequest request) {
        return ResponseEntity.ok(productService.getAllProductsByProviderId(requestHelper.getUserId(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}