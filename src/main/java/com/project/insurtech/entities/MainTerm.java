package com.project.insurtech.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "main_term")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "icon")
    private String icon;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;
}
