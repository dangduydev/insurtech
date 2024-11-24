package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.ClaimDTO;
import com.project.insurtech.entities.Claim;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface
ClaimMapper {
    ClaimDTO fromEntityToDTO(Claim claim);

}
