package com.project.insurtech.repositories;

import com.project.insurtech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.role.id = 3 " +
            "AND u.isDeleted = 0 " +
            "AND u.status = 1 " +
            "ORDER BY u.id DESC")
    List<User> findAllProviders();

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.role.id = 2 " +
            "AND u.isDeleted = 0 " +
            "AND u.status = 1 " +
            "ORDER BY u.id DESC")
    List<User> findAllUsers();
}
