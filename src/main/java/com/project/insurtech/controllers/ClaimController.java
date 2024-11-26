package com.project.insurtech.controllers;

import com.project.insurtech.components.helpers.PageHelper;
import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.dtos.ClaimDTO;
import com.project.insurtech.responses.ClaimListResponse;
import com.project.insurtech.responses.PageResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.Impl.ClaimService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final PageHelper pageHelper;

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

//    @GetMapping("/provider")
//    public ResponseEntity<ResponseObject> getClaimsByProviderId(HttpServletRequest request) {
//        try {
//            return ResponseEntity.ok(ResponseObject.builder()
//                    .message("Claims retrieved successfully")
//                    .status(HttpStatus.OK)
//                    .data(claimService.getClaimsByProviderId(requestHelper.getUserId(request)))
//                    .build());
//        } catch (Exception e) {
//            logger.error("Failed to get claims: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
//                    .message("Failed to get claims due to server error")
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .build());
//        }
//    }

    @GetMapping("/provider")
    public ResponseEntity<ResponseObject> getClaimsByProviderId(
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir,
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            Pageable pageableWithSort = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(sortDir.equalsIgnoreCase("desc")
                            ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)
            );
            Page<ClaimListResponse> claims = claimService.getClaimsByProviderId(
                    status,
                    customerName,
                    requestHelper.getUserId(request),
                    pageableWithSort
            );
            PageResponse<ClaimListResponse> response = pageHelper.toPagedResponse(claims);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Claims retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(response)
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
