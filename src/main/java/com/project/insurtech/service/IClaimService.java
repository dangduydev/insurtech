package com.project.insurtech.service;

import com.project.insurtech.dtos.ClaimDTO;
import com.project.insurtech.entities.Claim;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.responses.ClaimListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClaimService {
    ClaimDTO createClaim(ClaimDTO claim, Long userId) throws DataNotFoundException;

    List<ClaimDTO> getClaimsByUserId(Long userId) throws DataNotFoundException;

    Page<ClaimListResponse> getClaimsByProviderId(
            Integer status,
            String customerName,
            Long providerId,
            Pageable pageable
    ) throws DataNotFoundException;
}
