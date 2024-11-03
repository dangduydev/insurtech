package com.project.insurtech.repositories;

import com.project.insurtech.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProviderId(Long providerId);
}
