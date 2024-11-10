package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.responses.Product.ProductListResponse;
import com.project.insurtech.responses.Product.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "isDeleted", ignore = true)
    Product fromDTOtoEntity(ProductDTO productDTO);

    ProductDTO fromEntityToDTO(Product product);

    @Mapping(target = "mainTerms", source = "mainTerms")
    @Mapping(target = "sideTerms", source = "sideTerms")
    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse fromEntityToResponse(Product product);

    @Mapping(target = "icon", source = "provider.avatar")
    ProductListResponse fromEntityToProductListResponse(Product product);
//    void updateEntityFromDto(Product product, ProductDTO productDTO);
}