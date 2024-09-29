package com.project.insurtech.repository;

import com.project.insurtech.entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductTypeRepository extends JpaRepository<ProductType, Long> {
}
