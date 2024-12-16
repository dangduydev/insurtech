package com.project.insurtech.repositories;

import com.project.insurtech.entities.Claim;
import com.project.insurtech.responses.ClaimListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByCreatedByOrderByCreatedAtDesc(Long userId);

    @Query("""
            SELECT new com.project.insurtech.responses.ClaimListResponse(
                c.id,
                c.contractId,
                p.name,
                c.name,
                c.status,
                c.createdAt
            )
            FROM Claim c
            JOIN ContractDetail cd
                ON cd.contract.id = c.contractId
            JOIN Product p
                ON cd.productId = p.id
            JOIN User prov
                ON p.provider.id = prov.id
            WHERE p.provider.id = :providerId
            AND (:status IS NULL OR c.status = :status)
            AND (:customerName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')))
            """)
    Page<ClaimListResponse> findByProviderIdWithFilter(
            @Param("status") Integer status,
            @Param("customerName") String customerName,
            @Param("providerId") Long providerId,
            Pageable pageable
    );

}
