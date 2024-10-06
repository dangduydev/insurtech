package com.project.insurtech.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDTO {
    @NotNull(message = "Product's categoryId cannot be empty")
    private Long categoryId;

    @NotNull(message = "Product's providerId cannot be empty")
    private Long providerId;

    private Integer fromAge;
    private Integer toAge;

    @NotEmpty(message = "Product's status cannot be empty")
    private String status;

    @NotEmpty(message = "Product's name cannot be empty")
    private String name;
    private String description;
    private String applicableObject;
    private String scope;
    private String exclusion;
    private String thumbnail;
}
