package com.project.insurtech.specifications;

import com.project.insurtech.entities.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> withProviderId(Long providerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("provider").get("id"), providerId);
    }

    public static Specification<Product> withProviderIdAndFetchTerms(Long providerId) {
        return (root, query, criteriaBuilder) -> {
            // Ensure distinct results for LEFT JOIN FETCH
            root.fetch("mainTerms", JoinType.LEFT);
            root.fetch("sideTerms", JoinType.LEFT);
            query.distinct(true);

            // Add the where clause to filter by provider ID
            return criteriaBuilder.equal(root.get("provider").get("id"), providerId);
        };
    }
}
