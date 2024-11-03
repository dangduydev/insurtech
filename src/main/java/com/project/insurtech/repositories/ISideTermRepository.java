package com.project.insurtech.repositories;

import com.project.insurtech.entities.SideTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISideTermRepository extends JpaRepository<SideTerm, Long>, JpaSpecificationExecutor<SideTerm> {
}
