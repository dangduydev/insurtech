package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "isDeleted", ignore = true)
    Product fromDTOtoEntity(ProductDTO productDTO);

    ProductDTO fromEntityToDTO(Product product);

//    void updateEntityFromDto(Product product, ProductDTO productDTO);
}