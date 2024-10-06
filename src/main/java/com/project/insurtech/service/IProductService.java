package com.project.insurtech.service;


import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<ProductResponse> getAllProducts(String name, Long categoryId, Long providerId, String status, Pageable pageable);

    Product getProductById(Long id) throws DataNotFoundException;

    void createProduct(ProductDTO productDTO) throws Exception;

    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(Long id) throws DataNotFoundException;

}
