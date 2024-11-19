package com.project.insurtech.repositories;

import com.project.insurtech.entities.ContractMainTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IContractMainTermRepository extends JpaRepository<ContractMainTerm, Long> {
    List<ContractMainTerm> findByContractDetailId(Long contractDetailId);
}
