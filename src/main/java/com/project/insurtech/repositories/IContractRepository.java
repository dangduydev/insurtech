package com.project.insurtech.repositories;

import com.project.insurtech.entities.Contract;
import com.project.insurtech.responses.Contract.ContractListResponse;
import com.project.insurtech.responses.Contract.UserGetContractListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IContractRepository extends JpaRepository<Contract, Long>{
    Optional<List<Contract>> findByUserId(Long userId);

    @Query("""
                SELECT new com.project.insurtech.responses.Contract.UserGetContractListResponse(
                    c.id,
                    cat.name,
                    p.name,
                    prov.fullName,
                    c.status,
                    c.price,
                    c.startDate,
                    c.endDate
                )
                FROM Contract c
                JOIN ContractDetail cd ON c.id = cd.contract.id
                JOIN Product p ON cd.productId = p.id
                JOIN Category cat ON p.category.id = cat.id
                JOIN User prov ON p.provider.id = prov.id
                WHERE c.user.id = :userId
                ORDER BY c.createdAt DESC
            """)
    List<UserGetContractListResponse> findContractsByUserId(@Param("userId") Long userId);

    @Query("""
                SELECT new com.project.insurtech.responses.Contract.UserGetContractListResponse(
                    c.id,
                    cat.name,
                    p.name,
                    prov.fullName,
                    c.status,
                    c.price,
                    c.startDate,
                    c.endDate
                )
                FROM Contract c
                JOIN ContractDetail cd ON c.id = cd.contract.id
                JOIN Product p ON cd.productId = p.id
                JOIN Category cat ON p.category.id = cat.id
                JOIN User prov ON p.provider.id = prov.id
                WHERE prov.id = :providerId
                ORDER BY c.createdAt DESC
            """)
    List<UserGetContractListResponse> findContractsByProviderId(@Param("providerId") Long providerId);

    @Query("SELECT new com.project.insurtech.responses.Contract.ContractListResponse(" +
            "c.id, ca.name, p.name, u.fullName, c.status) " +
            "FROM Contract c " +
            "JOIN c.contractDetail cd " +
            "JOIN Product p ON cd.productId = p.id " +  // Thay đổi từ product sang productId
            "JOIN Category ca ON p.category.id = ca.id " +
            "JOIN User u ON c.user.id = u.id " +
            "WHERE p.provider.id = :providerId " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:categoryName IS NULL OR ca.name LIKE %:categoryName%) " +
            "AND (:productName IS NULL OR p.name LIKE %:productName%) " +
            "AND (:customerName IS NULL OR u.fullName LIKE %:customerName%)")
    Page<ContractListResponse> findByProviderWithFilters(
            @Param("providerId") Long providerId,
            @Param("status") Integer status,
            @Param("categoryName") String categoryName,
            @Param("productName") String productName,
            @Param("customerName") String customerName,
            Pageable pageable
    );

}
