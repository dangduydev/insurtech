package com.project.insurtech.repositories;

import com.project.insurtech.entities.ContractSideTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContractSideTermRepository extends JpaRepository<ContractSideTerm, Long> {
}
