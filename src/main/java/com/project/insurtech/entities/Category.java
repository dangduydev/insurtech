package com.project.insurtech.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;
}
