package com.project.insurtech.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class ProviderDTO {
    private String description;
    private String icon;

    @JsonProperty("phone_number")
    @NotEmpty(message = "Provider's phone number cannot be empty")
    private String phoneNumber;

    @JsonProperty("email")
    @NotEmpty(message = "Provider's email cannot be empty")
    private String email;

    @JsonProperty("fullname")
    private String fullName;

    private String address;

    @JsonProperty("password")
    @NotEmpty(message = "Provider's password cannot be empty")
    private String password;

    @JsonProperty("retype_password")
    @NotEmpty(message = "Provider's retype password cannot be empty")
    private String retypePassword;

}
