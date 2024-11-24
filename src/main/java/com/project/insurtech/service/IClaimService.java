package com.project.insurtech.service;

import com.project.insurtech.dtos.ClaimDTO;
import com.project.insurtech.entities.Claim;
import com.project.insurtech.exceptions.DataNotFoundException;

public interface IClaimService {
    ClaimDTO createClaim(ClaimDTO claim, Long userId) throws DataNotFoundException;
}
