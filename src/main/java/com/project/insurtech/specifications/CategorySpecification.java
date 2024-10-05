package com.project.insurtech.specifications;

import com.project.insurtech.entities.Category;
import com.project.insurtech.enums.IsDeletedEnum;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    public static Specification<Category> findByIdAndIsDeleted(Long id, IsDeletedEnum isDeleted) {
        return (root, query, criteriaBuilder) -> {
            Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id);
            Predicate deletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), isDeleted.getValue());
            return criteriaBuilder.and(idPredicate, deletedPredicate);
        };
    }

    public static Specification<Category> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Category> isNotDeleted(IsDeletedEnum isDeleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), isDeleted.getValue());
    }

}
