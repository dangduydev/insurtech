package com.project.insurtech.service;


import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.ProductResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    List<ProductResponse> getAllProductsByProviderId(Long providerId);

    Product getProductById(Long id) throws DataNotFoundException;

    Product createProduct(Long providerId, ProductDTO productDTO) throws Exception;

    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(Long id) throws DataNotFoundException;

}
