package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.MainTermDTO;
import com.project.insurtech.dtos.SideTermDTO;
import com.project.insurtech.entities.MainTerm;
import com.project.insurtech.entities.SideTerm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SideTermMapper {
    @Mapping(target = "isDeleted", ignore = true)
    SideTerm fromDTOtoEntity(SideTermDTO sideTermDTO);

    @Mapping(target = "productId", source = "product.id")
    SideTermDTO fromEntitiesToDTOs(SideTerm sideTerm);
}
