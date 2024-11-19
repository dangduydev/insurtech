package com.project.insurtech.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonBackReference
    private User provider;

    @Column(name = "from_age")
    private Integer fromAge;

    @Column(name = "to_age")
    private Integer toAge;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "applicable_object")
    private String applicableObject;

    @Column(name = "scope")
    private String scope;

    @Column(name = "exclusion")
    private String exclusion;

    @Column(name = "highlight")
    private String highlight;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MainTerm> mainTerms;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SideTerm> sideTerms;

//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    @JsonBackReference
//    private List<ContractDetail> contractDetails;
}
