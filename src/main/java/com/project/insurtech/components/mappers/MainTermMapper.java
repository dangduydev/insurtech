package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.MainTermDTO;
import com.project.insurtech.entities.MainTerm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MainTermMapper {

    @Mapping(target = "isDeleted", ignore = true)
    MainTerm fromDTOtoEntity(MainTermDTO mainTermDTO);

    @Mapping(target = "productId", source = "product.id")
    MainTermDTO fromEntitiesToDTOs(MainTerm mainTerm);
}
