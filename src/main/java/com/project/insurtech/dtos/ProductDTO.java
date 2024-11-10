package com.project.insurtech.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    @NotNull(message = "Product's categoryId cannot be empty")
    private Long categoryId;

    private Integer fromAge;
    private Integer toAge;

    @NotEmpty(message = "Product's status cannot be empty")
    private String status;

    @NotEmpty(message = "Product's name cannot be empty")
    private String name;
    private String description;
    private String gender;
    private Double price;
    private String applicableObject;
    private String scope;
    private String exclusion;
    private String highlight;
    private String thumbnail;
    private String attachment;

    @NotEmpty(message = "Product's mainTerms cannot be empty")
    List<MainTermDTO> mainTerms;

    @NotEmpty(message = "Product's sideTerms cannot be empty")
    List<SideTermDTO> sideTerms;

}
