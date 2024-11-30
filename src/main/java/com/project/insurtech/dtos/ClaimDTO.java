package com.project.insurtech.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {
    private Long id;
    private Long contractId;
    private Integer status;
    private Double amountClaim;
    private Double compensationAmount;
    private String note;
    private String description;
    private String upload;
    private String name;
    private String phone;
    private String email;
    private Integer requireUpdateStatus;
    private String bankAccount;
    private String bankName;
    private String bankBranch;
    private String bankNameOwner;
    private Long createdBy;
    private Long modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
