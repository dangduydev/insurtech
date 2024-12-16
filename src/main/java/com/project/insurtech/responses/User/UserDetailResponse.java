package com.project.insurtech.responses.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResponse {
    private String fullName;
    private String phoneNumber;
    private String address;
    private String email;
    private Date dateOfBirth;
    private String avatar;

}
