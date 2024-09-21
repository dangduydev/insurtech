package com.project.insurtech.dto.request.course;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseReq {
    @NotEmpty(message = "Course's name cannot be empty")
    private String title;
    private String description;
    private BigDecimal price;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("topic_id")
    private String topicId;
    @JsonProperty("teacher_id")
    private Integer teacherId;
}
