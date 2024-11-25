package com.project.insurtech.controllers;

import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.dtos.ClaimDTO;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.Impl.ClaimService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/claims")
public class ClaimController {
    private final Logger logger = LoggerFactory.getLogger(ClaimController.class);
    private final ClaimService claimService;
    private final RequestHelper requestHelper;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createClaim(@Valid @RequestBody ClaimDTO claim,
                                                      BindingResult result,
                                                      HttpServletRequest request) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Validation errors")
                    .status(HttpStatus.BAD_REQUEST)
                    .data(result.getAllErrors())
                    .build());
        }
        try {
            ClaimDTO response = claimService.createClaim(claim, requestHelper.getUserId(request));
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Claim created successfully")
                    .status(HttpStatus.OK)
                    .data(response)
                    .build());
        } catch (Exception e) {
            logger.error("Failed to create claim: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to create claim due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @GetMapping("user")
    public ResponseEntity<ResponseObject> getClaimsByUserId(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Claims retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(claimService.getClaimsByUserId(requestHelper.getUserId(request)))
                    .build());
        } catch (Exception e) {
            logger.error("Failed to get claims: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to get claims due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @GetMapping("/provider")
    public ResponseEntity<ResponseObject> getClaimsByProviderId(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Claims retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(claimService.getClaimsByProviderId(requestHelper.getUserId(request)))
                    .build());
        } catch (Exception e) {
            logger.error("Failed to get claims: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to get claims due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

}
