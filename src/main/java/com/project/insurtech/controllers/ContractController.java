package com.project.insurtech.controllers;

import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.dtos.ContractDTO;
import com.project.insurtech.entities.Contract;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Contract.UserGetContractListResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.Impl.ContractService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/contracts")
public class ContractController {
    private final Logger logger = LoggerFactory.getLogger(ContractController.class);
    private final ContractService contractService;
    private final RequestHelper requestHelper;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createContract(
            @Valid @RequestBody ContractDTO contractDTO,
            HttpServletRequest request

    ) {
        try {
            logger.info("Creating contract for userId: {}, productId: {}",
                    requestHelper.getUserId(request),
                    contractDTO.getContractDetailDTO().getProductId());
            Contract contract = contractService.createContract(contractDTO, requestHelper.getUserId(request));
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Contract created successfully")
                    .data(contract.getId())
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            logger.error("Data not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                    .message("Data not found: " + e.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } catch (Exception e) {
            logger.error("Failed to create contract: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to create contract due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseObject> getContractsByUserId(HttpServletRequest request) {
        try {
            List<UserGetContractListResponse> contracts = contractService.getContractsByUserId(requestHelper.getUserId(request));
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Contracts retrieved successfully")
                    .data(contracts)
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            logger.error("Data not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                    .message("Data not found: " + e.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } catch (Exception e) {
            logger.error("Failed to retrieve contracts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to retrieve contracts due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

}
