package com.project.insurtech.controller.course;

import org.springframework.data.domain.Page;

import com.project.insurtech.dto.request.course.CreateCourseReq;
import com.project.insurtech.dto.response.ResponseObject;
import com.project.insurtech.model.Course;
        
import com.project.insurtech.service.course.ICourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/courses")
public class CourseController {
    private final ICourseService courseService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createCourse(@Valid @RequestBody CreateCourseReq data,
                                                       BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        Course course = courseService.createCourse(data);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Create new course successfully")
                        .status(HttpStatus.CREATED)
                        .data(course)
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Course> coursePage = courseService.getCourses(page, size);
        
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("DS Khoa hoc123")
                        .status(HttpStatus.OK)
                        .data(coursePage)
                        .build()
        );
    }
}
