package com.project.insurtech.specifications;

import com.project.insurtech.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");  // Searching with LIKE
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categoryId"), categoryId);  // Filter by categoryId
    }

    public static Specification<Product> hasProviderId(Long providerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("providerId"), providerId);  // Filter by categoryId
    }

    public static Specification<Product> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);  // Filter by status
    }

    public static Specification<Product> hasDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), deleted);  // Filter by status
    }
}
