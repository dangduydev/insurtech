package com.project.insurtech.responses.Contract;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGetContractListResponse {
    private Long id;
    private String categoryName;
    private String productName;
    private String providerName;
    private Integer status;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
}
