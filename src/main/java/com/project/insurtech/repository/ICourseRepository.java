package com.project.insurtech.repository;

import com.project.insurtech.entities.Course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseRepository extends JpaRepository<Course, Long> {
}
