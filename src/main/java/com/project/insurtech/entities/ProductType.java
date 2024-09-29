package com.project.insurtech.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;


    @Column(name = "is_deleted")
    private int isDeleted;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;
}
