package com.project.insurtech.dtos;

import com.project.insurtech.entities.ContractMainTerm;
import com.project.insurtech.entities.ContractSideTerm;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractDetailDTO {
    private Long id;
    private Long contractId;
    private Long productId;
    private String productName;
    private String productDescription;
    private String applicableObject;
    private String scope;
    private String exclusion;
    private String name;
    private LocalDate dob;
    private String gender;
    private String identification;
    private String phone;
    private String email;
    private String address;
    //    @NotEmpty(message = "Product's mainTerms cannot be empty")
    List<MainTermDTO> mainTerms;

    //    @NotEmpty(message = "Product's sideTerms cannot be empty")
    List<SideTermDTO> sideTerms;
}
