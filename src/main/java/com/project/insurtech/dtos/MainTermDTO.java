package com.project.insurtech.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MainTermDTO {
    private Long id;

//    @NotEmpty(message = "Product's id cannot be empty")
    private Long productId;

//    @NotEmpty(message = "MainTerm's name cannot be empty")
    private String name;

    private String description;
    private Double amount;
    private String icon;
}
