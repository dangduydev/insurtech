package com.project.insurtech.responses;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimListResponse {
    private Long id;
    private Long contractId;
    private String productName;
    private String customerName;
    private Integer status;
    private LocalDateTime createdAt;
}
