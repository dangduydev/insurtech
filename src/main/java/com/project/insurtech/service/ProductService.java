package com.project.insurtech.service;

import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.enums.IsDeletedEnum;
import com.project.insurtech.exceptions.DataNotFoundException; // Create this exception class
import com.project.insurtech.components.mappers.ProductMapper;
import com.project.insurtech.repositories.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
    }

    @Override
    public void createProduct(ProductDTO productDTO) throws Exception {
        try {
            Product product = productMapper.fromDTOtoEntity(productDTO); // Use the mapper
            product.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
            productRepository.save(product);
        } catch (Exception e) {
            throw new Exception("Error creating product: " + e.getMessage());
        }
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
//        productMapper.updateEntityFromDto(product, productDTO); // Update using the mapper
        product.setModifiedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) throws DataNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
        product.setIsDeleted(IsDeletedEnum.DELETED.getValue()); // Or use a more sophisticated soft-delete mechanism
        productRepository.save(product);
    }
}
