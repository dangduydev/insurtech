package com.project.insurtech.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contract_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contract_id")
    @JsonBackReference
    private Contract contract;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 256)
    private String productName;

    @Column(name = "product_description", nullable = false, columnDefinition = "TEXT")
    private String productDescription;

    @Column(name = "applicable_object", length = 255)
    private String applicableObject;

    @Column(name = "scope", length = 255)
    private String scope;

    @Column(name = "exclusion", length = 255)
    private String exclusion;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "identification", nullable = false, length = 15)
    private String identification;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "created_by", length = 256)
    private Long createdBy;

    @Column(name = "modified_by", length = 256)
    private Long modifiedBy;

    @OneToMany(mappedBy = "contractDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContractMainTerm> contractMainTerms;

    @OneToMany(mappedBy = "contractDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContractSideTerm> contractSideTerms;
}