package com.project.insurtech.repositories;

import com.project.insurtech.entities.ContractSideTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IContractSideTermRepository extends JpaRepository<ContractSideTerm, Long> {
    List<ContractSideTerm> findByContractDetailId(Long contractDetailId);
}
