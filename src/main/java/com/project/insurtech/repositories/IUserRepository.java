package com.project.insurtech.repositories;

import com.project.insurtech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.role.id = :roleId " +
            "AND u.isDeleted = 0 " +
            "AND u.status = :status " +
            "ORDER BY u.id DESC")
    List<User> findAllUserByRoleIdAndStatus(@Param("roleId") long roleId, @Param("status") int status);

}
