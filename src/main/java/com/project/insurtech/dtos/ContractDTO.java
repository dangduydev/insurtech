package com.project.insurtech.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractDTO {
//    @NotBlank
    private String userId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Past
    private LocalDate dob;

    @NotBlank
    private String gender;

    @NotBlank
    @Size(max = 20)
    private String identification;

    @NotBlank
    @Size(max = 15)
    private String phone;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address;

//    @NotNull
    private Double price;

    @NotNull
    private ContractDetailDTO contractDetailDTO;
}