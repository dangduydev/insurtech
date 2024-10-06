package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.entities.Product;
import com.project.insurtech.responses.Product.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {
    @Mapping(target = "isDeleted", ignore = true)
    Product fromDTOtoEntity(ProductDTO productDTO);

    ProductDTO fromEntityToDTO(Product product);

    ProductResponse fromEntityToResponse(Product product);

    @Mapping(target = "providerId", ignore = true)
    void updateEntityFromDto(ProductDTO productDTO, @MappingTarget Product product);
}