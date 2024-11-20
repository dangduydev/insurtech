package com.project.insurtech.controllers;

import com.project.insurtech.components.helpers.PageHelper;
import com.project.insurtech.components.helpers.RequestHelper;
import com.project.insurtech.dtos.ContractDTO;
import com.project.insurtech.entities.Contract;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.Contract.ContractListResponse;
import com.project.insurtech.responses.Contract.UserGetContractListResponse;
import com.project.insurtech.responses.PageResponse;
import com.project.insurtech.responses.User.ResponseObject;
import com.project.insurtech.service.Impl.ContractService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/contracts")
public class ContractController {
    private final Logger logger = LoggerFactory.getLogger(ContractController.class);
    private final ContractService contractService;
    private final RequestHelper requestHelper;
    private final PageHelper pageHelper;

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

    @GetMapping("/{contractId}")
    public ResponseEntity<ResponseObject> getContractDetail(@PathVariable Long contractId,
                                                            HttpServletRequest request) {
        try {
            Contract contract = contractService.getContractDetail(contractId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Contract detail retrieved successfully")
                    .data(contract)
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException e) {
            logger.error("Data not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                    .message("Data not found: " + e.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } catch (Exception e) {
            logger.error("Failed to retrieve contract detail: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to retrieve contract detail due to server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @GetMapping("/provider")
    public ResponseEntity<PageResponse<ContractListResponse>> providerGetContractList(
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "productName", required = false) String productName,
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
            Page<ContractListResponse> contracts =
                    contractService.getContractsByProviderId(
                            status,
                            categoryName,
                            productName,
                            customerName,
                            requestHelper.getUserId(request),
                            pageableWithSort
                    );
            PageResponse<ContractListResponse> response = pageHelper.toPagedResponse(contracts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
