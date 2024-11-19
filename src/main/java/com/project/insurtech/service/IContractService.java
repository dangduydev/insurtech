package com.project.insurtech.service;

import com.project.insurtech.dtos.ContractDTO;
import com.project.insurtech.entities.Contract;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Contract.UserGetContractListResponse;

import java.util.List;

public interface IContractService {

    Contract createContract(ContractDTO contractDTO, Long userId) throws Exception;

    List<UserGetContractListResponse> getContractsByUserId(Long userId) throws DataNotFoundException;

    Contract getContractDetail(Long contractId) throws DataNotFoundException;
}
