package com.project.insurtech.responses.Product;

import com.project.insurtech.responses.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private Long categoryId;
    private Long providerId;
    private Integer fromAge;
    private Integer toAge;
    private String status;
    private String name;
    private String description;
    private String applicableObject;
    private String scope;
    private String exclusion;
    private String thumbnail;

}
