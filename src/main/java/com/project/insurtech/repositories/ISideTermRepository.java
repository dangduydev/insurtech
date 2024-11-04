package com.project.insurtech.repositories;

import com.project.insurtech.entities.SideTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ISideTermRepository extends JpaRepository<SideTerm, Long>, JpaSpecificationExecutor<SideTerm> {
    public List<SideTerm> findByProductId(Long productId);
}
