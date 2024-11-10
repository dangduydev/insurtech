package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.MainTermDTO;
import com.project.insurtech.dtos.ProductDTO;
import com.project.insurtech.dtos.SideTermDTO;
import com.project.insurtech.entities.MainTerm;
import com.project.insurtech.entities.Product;
import com.project.insurtech.entities.SideTerm;
import com.project.insurtech.responses.Product.ProductListResponse;
import com.project.insurtech.responses.Product.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "isDeleted", ignore = true)
    Product fromDTOtoEntity(ProductDTO productDTO);

    ProductDTO fromEntityToDTO(Product product);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "mainTerms", target = "mainTerms")
    @Mapping(source = "sideTerms", target = "sideTerms")
    ProductResponse fromEntityToResponse(Product product);

//    // Custom mappings for nested lists
//    List<MainTermDTO> mapMainTerms(List<MainTerm> mainTerms);
//    List<SideTermDTO> mapSideTerms(List<SideTerm> sideTerms);

    @Mapping(target = "icon", source = "provider.avatar")
    ProductListResponse fromEntityToProductListResponse(Product product);
//    void updateEntityFromDto(Product product, ProductDTO productDTO);
}