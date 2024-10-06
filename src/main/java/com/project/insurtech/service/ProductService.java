package com.project.insurtech.service;

import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Category;
import com.project.insurtech.entities.Product;
import com.project.insurtech.enums.IsDeletedEnum;
import com.project.insurtech.exceptions.DataNotFoundException; // Create this exception class
import com.project.insurtech.components.mappers.ProductMapper;
import com.project.insurtech.repositories.ICategoryRepository;
import com.project.insurtech.repositories.IProductRepository;
import com.project.insurtech.responses.Product.ProductResponse;
import com.project.insurtech.specifications.CategorySpecification;
import com.project.insurtech.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    @Override
    public Page<ProductResponse> getAllProducts(String name, Long categoryId, Long providerId, String status, Pageable pageable) {
        Specification<Product> specification = Specification.where(null);
        if (name != null) {
            specification = specification.and(ProductSpecification.hasNameLike(name));
        }
        if (categoryId != null) {
            specification = specification.and(ProductSpecification.hasCategoryId(categoryId));
        }
        if (providerId != null) {
            specification = specification.and(ProductSpecification.hasProviderId(providerId));
        }
        if (status != null) {
            specification = specification.and(ProductSpecification.hasStatus(status));
        }
        specification = specification.and(ProductSpecification.hasDeleted(IsDeletedEnum.NOT_DELETED.getValue()));
        // Apply sorting from Pageable object
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(product -> {
            ProductResponse response = productMapper.fromEntityToResponse(product);
            response.setCreatedAt(product.getCreatedAt());
            response.setModifiedAt(product.getModifiedAt());
            return response;
        });
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
        Optional<Product> optionalProduct = productRepository.findByIdAndIsDeletedFalse(id);
        Product product = optionalProduct.orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));

        // Check if category exists and is not deleted
        if (productDTO.getCategoryId() != null) {
            Specification<Category> specification = Specification.where(
                            CategorySpecification.hasId(productDTO.getCategoryId()))
                    .and(CategorySpecification.isNotDeleted(IsDeletedEnum.NOT_DELETED));
            boolean categoryExists = categoryRepository.findOne(specification).isPresent();
            if (!categoryExists) {
                throw new DataNotFoundException("Category not found with id: " + productDTO.getCategoryId());
            }
        }
        productMapper.updateEntityFromDto(productDTO, product);
        product.setModifiedAt(LocalDateTime.now());
        //Todo: update modifiedBy
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
