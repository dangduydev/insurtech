package com.project.insurtech.service.Impl;

import com.project.insurtech.components.mappers.ClaimMapper;
import com.project.insurtech.dtos.ClaimDTO;
import com.project.insurtech.entities.Claim;
import com.project.insurtech.entities.Contract;
import com.project.insurtech.entities.User;
import com.project.insurtech.enums.ClaimStatusEnum;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.repositories.IClaimRepository;
import com.project.insurtech.repositories.IContractRepository;
import com.project.insurtech.repositories.IUserRepository;
import com.project.insurtech.service.IClaimService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClaimService implements IClaimService {
    private final Logger logger = LoggerFactory.getLogger(ClaimService.class);
    private final IContractRepository contractRepository;
    private final IUserRepository userRepository;
    private final IClaimRepository claimRepository;
    private final ClaimMapper claimMapper;


    @Override
    public ClaimDTO createClaim(ClaimDTO claimDTO, Long userId) throws DataNotFoundException {
        logger.info("Creating claim with user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new DataNotFoundException("User not found with id: " + userId));
        Contract contract = contractRepository.findById(claimDTO.getContractId())
                .orElseThrow(() ->
                        new DataNotFoundException("Contract not found with id: " + claimDTO.getContractId()));
        Claim claim = new Claim().builder()
                .status(ClaimStatusEnum.PENDING.getValue())
                .contractId(contract.getId())
                .amountClaim(claimDTO.getAmountClaim())
                .compensationAmount(0.0)
                .note(claimDTO.getNote())
                .description(claimDTO.getDescription())
                .upload(claimDTO.getUpload())
                .name(claimDTO.getName())
                .phone(claimDTO.getPhone())
                .email(claimDTO.getEmail())
                .bankAccount(claimDTO.getBankAccount())
                .bankName(claimDTO.getBankName())
                .bankBranch(claimDTO.getBankBranch())
                .bankNameOwner(claimDTO.getBankNameOwner())
                .createdBy(user.getId())
                .modifiedBy(user.getId())
                .build();
        claim = claimRepository.save(claim);
        return claimMapper.fromEntityToDTO(claim);
    }
}
