package com.project.insurtech.repositories;

import com.project.insurtech.entities.Contract;
import com.project.insurtech.responses.Contract.UserGetContractListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IContractRepository extends JpaRepository<Contract, Long> {
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



}
