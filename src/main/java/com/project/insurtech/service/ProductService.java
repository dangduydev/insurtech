package com.project.insurtech.service;

import com.project.insurtech.components.mappers.MainTermMapper;
import com.project.insurtech.components.mappers.SideTermMapper;
import com.project.insurtech.dtos.MainTermDTO;
import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.dtos.SideTermDTO;
import com.project.insurtech.entities.*;
import com.project.insurtech.enums.IsDeletedEnum;
import com.project.insurtech.exceptions.DataNotFoundException; // Create this exception class
import com.project.insurtech.components.mappers.ProductMapper;
import com.project.insurtech.repositories.*;
import com.project.insurtech.responses.ProductResponse;
import com.project.insurtech.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private static final Logger logger = LoggerFactory.getLogger(IProductService.class);
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ICategoryRepository categoryRepository;
    private final MainTermMapper mainTermMapper;
    private final IMainTermRepository mainTermRepository;
    private final ISideTermRepository sideTermRepository;
    private final SideTermMapper sideTermMapper;


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductResponse> getAllProductsByProviderId(Long providerId) {
        List<Product> products = productRepository.findByProviderId(providerId);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse response = productMapper.fromEntityToResponse(product);

            // Lấy các MainTerm và SideTerm theo product ID
            List<MainTerm> mainTerms = mainTermRepository.findByProductId(product.getId());
            List<SideTerm> sideTerms = sideTermRepository.findByProductId(product.getId());

            response.setMainTerms(mainTerms.stream().map(mainTermMapper::fromEntitiesToDTOs).toList());
            response.setSideTerms(sideTerms.stream().map(sideTermMapper::fromEntitiesToDTOs).toList());

            productResponses.add(response);
        }

        return productResponses;
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public Product createProduct(Long providerId, ProductDTO productDTO) throws DataNotFoundException {
        // Log the start of the process
        logger.info("Creating product and associated terms for category ID: {}", productDTO.getCategoryId());

        // Check if category exists
        Category category = categoryRepository.findByIdAndIsDeletedAndProviderId(
                        productDTO.getCategoryId(),
                        IsDeletedEnum.NOT_DELETED.getValue(),
                        providerId)
                .orElseThrow(() -> new DataNotFoundException("Category not found with id: " + productDTO.getCategoryId()));

        // Map ProductDTO to Product entity
        Product product = productMapper.fromDTOtoEntity(productDTO);
        product.setCategoryId(category.getId());
        product.setProvider(category.getProvider());
        product.setIsDeleted(false);

        // Save the product and retrieve its ID
        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getId());

        // Handle MainTerms
        List<MainTermDTO> mainTermDTOs = productDTO.getMainTerms();
        if (mainTermDTOs != null && !mainTermDTOs.isEmpty()) {
            for (MainTermDTO mainTermDTO : mainTermDTOs) {
                MainTerm mainTerm = mainTermMapper.fromDTOtoEntity(mainTermDTO);
                mainTerm.setProduct(savedProduct); // Set the Product entity directly
                mainTerm.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
                mainTermRepository.save(mainTerm);
                logger.info("MainTerm created for product ID: {}", savedProduct.getId());
            }
        }

        // Handle SideTerms (similar logic for SideTerm as above)
        List<SideTermDTO> sideTermDTOs = productDTO.getSideTerms();
        if (sideTermDTOs != null && !sideTermDTOs.isEmpty()) {
            for (SideTermDTO sideTermDTO : sideTermDTOs) {
                SideTerm sideTerm = sideTermMapper.fromDTOtoEntity(sideTermDTO);
                sideTerm.setProduct(savedProduct); // Set the Product entity directly
                sideTerm.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
                sideTermRepository.save(sideTerm);
                logger.info("SideTerm created for product ID: {}", savedProduct.getId());
            }
        }

        return savedProduct;
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
