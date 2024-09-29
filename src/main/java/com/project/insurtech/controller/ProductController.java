package com.project.insurtech.controller;

import com.project.insurtech.responses.ProductTypeListResponse;
import com.project.insurtech.responses.ResponseObject;
import com.project.insurtech.entities.ProductType;
import com.project.insurtech.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/product_types")
    public ResponseEntity<List<ProductType>> getProductTypes() {
        List<ProductType> productTypes = productService.getProductTypes();
        return ResponseEntity.ok(productTypes);
    }
}
