package com.project.insurtech.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MainTermDTO {
    @NotEmpty(message = "Product's id cannot be empty")
    private Long product_id;

    @NotEmpty(message = "MainTerm's name cannot be empty")
    private String name;

    private String description;
    private Double amount;
    private String icon;
}
