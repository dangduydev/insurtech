package com.project.insurtech.responses.Contract;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractListResponse {
    private Long id;
    private String categoryName;
    private String productName;
    private String customerName;
    private Integer status;
}
