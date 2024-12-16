package com.project.insurtech.repositories;

import com.project.insurtech.entities.MainTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IMainTermRepository extends JpaRepository<MainTerm, Long>, JpaSpecificationExecutor<MainTerm> {
    List<MainTerm> findByProductId(Long productId);

}
