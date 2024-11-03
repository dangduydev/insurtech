package com.project.insurtech.controllers;

import com.project.insurtech.dtos.ProviderDTO;
import com.project.insurtech.entities.User;
import com.project.insurtech.responses.User.RegisterResponse;
import com.project.insurtech.service.ICategoryService;
import com.project.insurtech.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/providers")
public class ProviderController {
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
    private final IUserService userService;

    @PostMapping("")
    public ResponseEntity<RegisterResponse> createUser(
            @Valid @RequestBody ProviderDTO providerDTO,
            BindingResult result
    ) {
        RegisterResponse registerResponse = new RegisterResponse();
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            registerResponse.setMessage(errorMessages.toString());
            return ResponseEntity.badRequest().body(registerResponse);
        }
        try {
            User user = userService.createProvider(providerDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(RegisterResponse.builder()
                            .message("User created successfully")
                            .user(user)
                            .build());
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
