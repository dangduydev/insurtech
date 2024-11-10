package com.project.insurtech.responses.Product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListResponse {
    private Long id;
    private String name;
    private String highlight;
    private Double price;
    private String thumbnail;
    private String icon;
}
