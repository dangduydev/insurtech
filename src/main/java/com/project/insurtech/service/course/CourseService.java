package com.project.insurtech.service.course;

import com.project.insurtech.common.StaticEnum;

import com.project.insurtech.dto.request.course.CreateCourseReq;

import com.project.insurtech.model.Course;
import com.project.insurtech.repository.course.ICourseRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {
    private final ICourseRepository courseRepository;

    @Override
    public Course createCourse(CreateCourseReq createCourseReq) {
        Course course = Course
                .builder()
                .title(createCourseReq.getTitle())
                .topicId(createCourseReq.getTopicId())
                .teacherId(createCourseReq.getTeacherId())
                .description(createCourseReq.getDescription())
                .price(createCourseReq.getPrice())
                .imageUrl(createCourseReq.getImageUrl())
                .status(StaticEnum.CourseStatus.INIT.getCode())
                .build();
        return courseRepository.save(course);
    }

    @Override
    public Page<Course> getCourses(int page, int size) {
        Page<Course> coursePage = courseRepository.findAll(PageRequest.of(page, size));
        return coursePage;
    }
}
