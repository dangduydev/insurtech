package com.project.insurtech.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "contract_side_term")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractSideTerm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_detail_id")
    @JsonBackReference
    private ContractDetail contractDetail;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "description", nullable = false, length = 256)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_by", length = 256)
    private Long createdBy;

    @Column(name = "modified_by", length = 256)
    private Long modifiedBy;
}