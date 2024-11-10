package com.project.insurtech.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
//    @JsonIgnore
    private Product product;

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

    // @CreationTimestamp
    // @Column(name = "created_at")
    // private LocalDateTime createdAt;

    // @UpdateTimestamp
    // @Column(name = "modified_at")
    // private LocalDateTime modifiedAt;
}
