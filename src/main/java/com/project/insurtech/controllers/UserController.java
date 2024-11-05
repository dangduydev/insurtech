package com.project.insurtech.controllers;

import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.dtos.UserDTO;
import com.project.insurtech.entities.User;
import com.project.insurtech.responses.User.LoginResponse;
import com.project.insurtech.responses.User.RegisterResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.responses.User.UserDetailResponse;
import com.project.insurtech.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("${api.prefix}/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RequestHelper requestHelper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(
            @Valid @RequestBody UserDTO userDTO,
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
            User user = userService.createUser(userDTO);
            registerResponse.setUser(user);
            registerResponse.setMessage("User created successfully");
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody UserDTO userDTO
    ) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            //check login
            String token = userService.login(
                    userDTO.getPhoneNumber(),
                    userDTO.getPassword(),
                    userDTO.getRoleId()
            );

            loginResponse.setMessage("Login successful");
            loginResponse.setToken(token);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            loginResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(loginResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUserDetail(@PathVariable Long id) {
        try {
            logger.info("Start get User detail with ID: {}", id);
            UserDetailResponse userDetailResponse = userService.getUserDetail(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("User details retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(userDetailResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Failed to retrieve user details")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }
}
