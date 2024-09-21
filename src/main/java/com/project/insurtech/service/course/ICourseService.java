package com.project.insurtech.service.course;

import org.springframework.data.domain.Page;

import com.project.insurtech.dto.request.course.CreateCourseReq;
import com.project.insurtech.model.Course;


public interface ICourseService {
    Course createCourse(CreateCourseReq createCourseReq);

    Page<Course> getCourses(int page, int size);
}
