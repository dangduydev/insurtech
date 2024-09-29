package com.project.insurtech.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.insurtech.entities.ProductType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTypeListResponse {
    private String message;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("product_type")
    private ProductType productType;

}
