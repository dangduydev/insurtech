package com.project.insurtech.components.mappers;

import com.project.insurtech.dtos.ContractDTO;
import com.project.insurtech.dtos.ContractDetailDTO;
import com.project.insurtech.entities.Contract;
import com.project.insurtech.entities.ContractDetail;
import com.project.insurtech.entities.ContractMainTerm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ContractMapper {

    Contract fromDTOtoEntity(ContractDTO contractDTO);

    ContractDetail fromDTOtoEntity(ContractDetailDTO contractDetailDTO);

    ContractDTO fromEntityToDTO(Contract contract);

    ContractDetailDTO fromEntityToDTO(ContractDetail contractDetail);

}
