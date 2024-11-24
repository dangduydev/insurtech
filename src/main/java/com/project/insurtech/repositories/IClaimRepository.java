package com.project.insurtech.repositories;

import com.project.insurtech.entities.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClaimRepository extends JpaRepository<Claim, Long> {
}
