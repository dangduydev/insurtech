package com.project.insurtech.repositories;

import com.project.insurtech.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface IProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findProviderByUserId(@Param("user_id") Long userId);
}

