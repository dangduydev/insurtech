package com.project.insurtech.responses;

import com.project.insurtech.dtos.MainTermDTO;
import com.project.insurtech.dtos.SideTermDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private Long categoryId;
    private Integer fromAge;
    private Integer toAge;
    private String status;
    private String name;
    private String description;
    private String gender;
    private String applicableObject;
    private String scope;
    private String exclusion;
    private Double price;
    private String thumbnail;
    private String attachment;
    List<MainTermDTO> mainTerms;
    List<SideTermDTO> sideTerms;
}
