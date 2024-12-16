package com.project.insurtech.specifications;

import com.project.insurtech.entities.Product;
import com.project.insurtech.entities.User;
import com.project.insurtech.enums.GenderEnum;
import com.project.insurtech.enums.IsDeletedEnum;
import com.project.insurtech.enums.ProductStatusEnum;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductSpecification {

    public static Specification<Product> userGetProductSpecification(
            Long categoryId,
            Long providerId,
            String gender
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (providerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("provider").get("id"), providerId));
            }
            if (StringUtils.hasText(gender)) {
                if (gender.equals(GenderEnum.MALE.getValue())) {
                    // If gender is 'MALE', search for 'MALE' or 'ALL'
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("gender"), GenderEnum.MALE.getValue()),
                            criteriaBuilder.equal(root.get("gender"), "ALL")
                    ));
                } else if (gender.equals(GenderEnum.FEMALE.getValue())) {
                    // If gender is 'FEMALE', search for 'FEMALE' or 'ALL'
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("gender"), GenderEnum.FEMALE.getValue()),
                            criteriaBuilder.equal(root.get("gender"), "ALL")
                    ));
                }
            }
            // Filter non-deleted, available products
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), IsDeletedEnum.NOT_DELETED.getValue()));
            predicates.add(criteriaBuilder.equal(root.get("status"), ProductStatusEnum.AVAILABLE.getValue()));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> productsByProviders(List<Long> providerIds) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<Product, User> providerJoin = root.join("provider", JoinType.INNER);

            query.groupBy(providerJoin.get("id"), providerJoin.get("fullName"));
            query.multiselect(
                    providerJoin.get("id"),
                    providerJoin.get("fullName"),
                    cb.count(root.get("id"))
            );

            return providerJoin.get("id").in(providerIds);
        };
    }

}
