package com.project.insurtech.components.helpers;

import com.project.insurtech.responses.User.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidationHelper {

    public ResponseEntity<ResponseObject> handleValidationErrors(BindingResult result) {
        List<String> errorMessages = result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(ResponseObject.builder()
                .message("Validation failed: " + String.join(", ", errorMessages))
                .status(HttpStatus.BAD_REQUEST)
                .build());
    }
}
