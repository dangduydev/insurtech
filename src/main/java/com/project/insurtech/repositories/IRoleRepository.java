package com.project.insurtech.repositories;

import com.project.insurtech.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
}
