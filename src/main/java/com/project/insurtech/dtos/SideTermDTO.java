package com.project.insurtech.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SideTermDTO {
    private Long id;
    
//    @NotEmpty(message = "SideTerm's productId cannot be empty")
    private Long productId;

//    @NotEmpty(message = "MainTerm's name cannot be empty")
    private String name;

    private String description;
    private Double amount;
    private Double price;
    private String icon;
}
