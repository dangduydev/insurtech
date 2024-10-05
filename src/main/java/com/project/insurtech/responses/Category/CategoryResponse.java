package com.project.insurtech.responses.Category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.insurtech.entities.Category;
import com.project.insurtech.responses.BaseResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse extends BaseResponse {
    private String message;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("category")
    private Category category;

}
