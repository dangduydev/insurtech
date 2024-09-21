package com.project.insurtech.repository.course;

import com.project.insurtech.model.Course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseRepository extends JpaRepository<Course, Long> {
}
