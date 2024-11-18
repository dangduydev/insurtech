package com.project.insurtech.repositories;

import com.project.insurtech.entities.Product;
import com.project.insurtech.entities.Role;
import com.project.insurtech.responses.Provider.ProviderProductResponse;
import com.project.insurtech.specifications.ProductSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @PersistenceContext
    EntityManager entityManager = null;

    List<Product> findByProviderId(Long providerId);

    @Query("SELECT NEW com.project.insurtech.responses.Provider.ProviderProductResponse(" +
            "p.provider.id, u.fullName, COUNT(p.id)) " +
            "FROM Product p " +
            "JOIN User u ON p.provider.id = u.id " +
            "WHERE p.isDeleted = false " +
            "AND u.id IN :providerIds " +
            "GROUP BY p.provider.id, u.fullName")
    List<ProviderProductResponse> countProductsByProviders(@Param("providerIds") List<Long> providerIds);

    default List<ProviderProductResponse> countProductsByProvider(List<Long> providerIds) {
        // Sử dụng CriteriaBuilder và CriteriaQuery để xây dựng truy vấn
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProviderProductResponse> query = cb.createQuery(ProviderProductResponse.class);
        Root<Product> product = query.from(Product.class);

        // Áp dụng Specification vào CriteriaQuery
        Specification<Product> spec = ProductSpecification.productsByProviders(providerIds);
        query.where(spec.toPredicate(product, query, cb));

        // Thực thi truy vấn và trả về kết quả
        return entityManager.createQuery(query).getResultList();
    }

}
