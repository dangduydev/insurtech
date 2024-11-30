package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.MainTermDTO;
import com.project.insurtech.entities.MainTerm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MainTermMapper {

    @Mapping(target = "isDeleted", ignore = true)
    MainTerm fromDTOtoEntity(MainTermDTO mainTermDTO);

    @Mapping(target = "productId", source = "product.id")
    MainTermDTO fromEntitiesToDTOs(MainTerm mainTerm);

    @Mapping(target = "id", source = "mainTern.id")
    List<MainTerm> fromDTOsToEntities(List<MainTermDTO> mainTermDTOs);

    void updateEntityFromDto(MainTermDTO mainTermDTO, @MappingTarget MainTerm mainTerm);
}
