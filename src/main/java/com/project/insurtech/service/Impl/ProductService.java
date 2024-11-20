package com.project.insurtech.service.Impl;

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
import com.project.insurtech.responses.Product.ProductListResponse;
import com.project.insurtech.responses.Product.ProductResponse;
import com.project.insurtech.responses.Provider.ProviderProductResponse;
import com.project.insurtech.service.IProductService;
import com.project.insurtech.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final IUserRepository userRepository;


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductResponse> getAllProductsByProviderId(Long providerId) {
        List<Product> products = productRepository.findByProviderId(providerId);
        return products.stream()
                .map(productMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) throws DataNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
        return productMapper.fromEntityToResponse(product);
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
        product.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
        product.setCategory(category);
        product.setProvider(category.getProvider());
        product.setCreatedAt(LocalDateTime.now());
        product.setModifiedAt(LocalDateTime.now());

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
//                mainTerm.setCreatedAt(LocalDateTime.now());
//                mainTerm.setModifiedAt(LocalDateTime.now());
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
//                sideTerm.setCreatedAt(LocalDateTime.now());
//                sideTerm.setModifiedAt(LocalDateTime.now());
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

    @Override
    public Page<ProductListResponse> getFilteredProducts(
            Long categoryId,
            Long providerId,
            String gender,
            Pageable pageable
    ) {
        return productRepository
                .findAll(ProductSpecification.userGetProductSpecification(categoryId, providerId, gender), pageable)
                .map(productMapper::fromEntityToProductListResponse);
    }

    @Override
    public List<ProviderProductResponse> getProductCountByProvider() {
        List<User> providers = userRepository.findAllProviders();
        if (providers.isEmpty()) {
            return Collections.emptyList();
        }

        return productRepository.countProductsByProviders(
                providers.stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
        // Lấy danh sách providers từ User repository
//        List<User> providers = userRepository.findAllProviders();
//        if (providers.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        // Lấy danh sách providerId và gọi repository để đếm sản phẩm
//        List<Long> providerIds = providers.stream()
//                .map(User::getId)
//                .collect(Collectors.toList());
//
//        return productRepository.countProductsByProviders(providerIds);
    }

}
