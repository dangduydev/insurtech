package com.project.insurtech.responses.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.insurtech.entities.User;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}
