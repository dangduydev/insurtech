package com.project.insurtech.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Claim extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id", nullable = false, length = 256)
    private Long contractId;

    @Column(name = "status", nullable = false, columnDefinition = "int default 1")
    private Integer status;

    @Column(name = "amount_claim", nullable = false)
    private Double amountClaim;

    @Column(name = "compensation_amount", nullable = false)
    private Double compensationAmount;

    @Column(name = "note", columnDefinition = "text")
    private String note;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "upload", columnDefinition = "text")
    private String upload;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "phone", nullable = false, length = 256)
    private String phone;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "require_update_status", nullable = false, columnDefinition = "int default 0")
    private Integer requireUpdateStatus;

    @Column(name = "bank_account", length = 256)
    private String bankAccount;

    @Column(name = "bank_name", length = 256)
    private String bankName;

    @Column(name = "bank_branch", length = 256)
    private String bankBranch;

    @Column(name = "bank_name_owner", length = 256)
    private String bankNameOwner;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "modified_by")
    private Long modifiedBy;
}