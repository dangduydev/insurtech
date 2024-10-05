package com.project.insurtech.service;


import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.exceptions.DataNotFoundException;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id) throws DataNotFoundException;

    void createProduct(ProductDTO productDTO) throws Exception;

    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(Long id) throws DataNotFoundException;

}
