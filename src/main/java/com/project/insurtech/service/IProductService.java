package com.project.insurtech.service;


import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Product.ProductListResponse;
import com.project.insurtech.responses.Product.ProductResponse;
import com.project.insurtech.responses.Provider.ProviderProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IProductService {
    List<Product> getAllProducts();

    List<ProductResponse> getAllProductsByProviderId(Long providerId);

    ProductResponse getProductById(Long id) throws DataNotFoundException;

    Product createProduct(Long providerId, ProductDTO productDTO) throws Exception;

    Product updateProduct(Long id, ProductDTO productDTO, Long userId) throws DataNotFoundException;

    void deleteProduct(Long id) throws DataNotFoundException;

    //Mobile API
    Page<ProductListResponse> getFilteredProducts(
            List<Long> providerId,
            String productName,
            Pageable pageable);

    List<ProviderProductResponse> getProductCountByProvider();

}
