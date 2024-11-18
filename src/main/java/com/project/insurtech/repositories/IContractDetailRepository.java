package com.project.insurtech.repositories;

import com.project.insurtech.entities.ContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContractDetailRepository extends JpaRepository<ContractDetail, Long> {
}
