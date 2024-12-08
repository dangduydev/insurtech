package com.project.insurtech.controllers;

import com.project.insurtech.dtos.ProviderDTO;
import com.project.insurtech.responses.User.RegisterResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.Impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admins")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final UserService userService;

    @PostMapping("/provider")
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
            userService.createProvider(providerDTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(RegisterResponse.builder()
                            .message("Failed to create user cause by: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/provider")
    public ResponseEntity<ResponseObject> getProviders() {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(userService.getAllProviders())
                    .message("Providers retrieved successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            logger.error("Error getting providers: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .message("Failed to get providers cause by: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseObject> getUsers() {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(userService.getAllUsers())
                    .message("Users retrieved successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            logger.error("Error getting users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .message("Failed to get users cause by: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseObject> getProviderDetail(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(userService.getUserDetail(id))
                    .message("User detail retrieved successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            logger.error("Error getting provider detail: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .message("Failed to get provider detail cause by: " + e.getMessage())
                            .build());
        }
    }
}
