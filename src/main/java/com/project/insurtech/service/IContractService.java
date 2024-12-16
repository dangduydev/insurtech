package com.project.insurtech.service;

import com.project.insurtech.dtos.ContractDTO;
import com.project.insurtech.entities.Contract;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Contract.ContractListResponse;
import com.project.insurtech.responses.Contract.UserGetContractListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IContractService {

    Contract createContract(ContractDTO contractDTO, Long userId) throws Exception;

    List<UserGetContractListResponse> getContractsByUserId(Long userId) throws DataNotFoundException;

    Contract getContractDetail(Long contractId) throws DataNotFoundException;

    Page<ContractListResponse> getContractsByProviderId(
            Integer status,
            String categoryName,
            String productName,
            String customerName,
            Long providerId,
            Pageable pageable
    );
}
