package com.project.insurtech.responses.Category;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.insurtech.entities.Category;
import com.project.insurtech.responses.BaseResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isDeleted;
    private Long createdBy;
    private Long modifiedBy;
//    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
//    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;
}

