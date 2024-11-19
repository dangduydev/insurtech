package com.project.insurtech.repositories;

import com.project.insurtech.dtos.ContractDetailDTO;
import com.project.insurtech.entities.ContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IContractDetailRepository extends JpaRepository<ContractDetail, Long> {
    Optional<ContractDetail> findByContractId(Long contractId);
}
