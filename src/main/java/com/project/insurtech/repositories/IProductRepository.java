package com.project.insurtech.repositories;

import com.project.insurtech.entities.Product;
import com.project.insurtech.responses.Provider.ProviderProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {


    List<Product> findByProviderId(Long providerId);

    @Query("SELECT NEW com.project.insurtech.responses.Provider.ProviderProductResponse(" +
            "p.provider.id, u.fullName, COUNT(p.id)) " +
            "FROM Product p " +
            "JOIN User u ON p.provider.id = u.id " +
            "WHERE p.isDeleted = false " +
            "AND u.id IN :providerIds " +
            "GROUP BY p.provider.id, u.fullName")
    List<ProviderProductResponse> countProductsByProviders(@Param("providerIds") List<Long> providerIds);

}
