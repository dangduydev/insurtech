package com.project.insurtech.repositories;

import com.project.insurtech.entities.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByCreatedByOrderByCreatedAtDesc(Long userId);

@Query("""
        SELECT c
        FROM Claim c
        JOIN ContractDetail cd
        ON cd.contract.id = c.contractId
        JOIN Product p
        ON cd.productId = p.id
        WHERE p.provider.id = :providerId
        """)
    List<Claim> findByProviderId(@Param("providerId") Long providerId);
}
