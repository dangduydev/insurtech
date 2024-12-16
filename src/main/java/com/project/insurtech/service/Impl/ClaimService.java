package com.project.insurtech.service.Impl;

import com.project.insurtech.components.mappers.ClaimMapper;
import com.project.insurtech.dtos.ClaimDTO;
import com.project.insurtech.entities.*;
import com.project.insurtech.enums.ClaimStatusEnum;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.repositories.*;
import com.project.insurtech.responses.ClaimListResponse;
import com.project.insurtech.service.IClaimService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService implements IClaimService {
    private final Logger logger = LoggerFactory.getLogger(ClaimService.class);
    private final IContractRepository contractRepository;
    private final IUserRepository userRepository;
    private final IClaimRepository claimRepository;
    private final ClaimMapper claimMapper;
    private final IContractDetailRepository contractDetailRepository;
    private final IProductRepository productRepository;


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

    @Override
    public List<ClaimDTO> getClaimsByUserId(Long userId) throws DataNotFoundException {
        logger.info("Getting claims by user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new DataNotFoundException("User not found with id: " + userId));
        List<Claim> claims = claimRepository.findByCreatedByOrderByCreatedAtDesc(user.getId());
        return claimMapper.fromEntityToDTO(claims);
    }

    @Override
    public Page<ClaimListResponse> getClaimsByProviderId(
            Integer status,
            String customerName,
            Long providerId,
            Pageable pageable
    ) throws DataNotFoundException {
        logger.info("Getting claims by provider id: {}", providerId);
        return claimRepository.findByProviderIdWithFilter(
                status,
                customerName,
                providerId,
                pageable
        );
    }

    @Override
    public ClaimDTO getClaimDetail(Long claimId, Long userId) throws DataNotFoundException {
        logger.info("Getting claim detail with claim id: {}", claimId);
        User provider = userRepository.findById(userId)
                .orElseThrow(() ->
                        new DataNotFoundException("User not found with id: " + userId));
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new DataNotFoundException("Claim not found with id: " + claimId));
        ContractDetail contractDetail = contractDetailRepository.findByContractId(claim.getContractId())
                .orElseThrow(() ->
                        new DataNotFoundException("Contract detail not found with claim id: " + claimId));
        Product product = productRepository.findById(contractDetail.getProductId())
                .orElseThrow(() ->
                        new DataNotFoundException("Product not found with id: " + contractDetail.getProductId()));
        if (!product.getProvider().getId().equals(provider.getId())) {
            throw new DataNotFoundException("Claim not found with id: " + claimId);
        }
        return claimMapper.fromEntityToDTO(claim);
    }

    @Override
    public ClaimDTO updateClaimStatus(Long claimId, Integer status, Long userId) throws DataNotFoundException {
        logger.info("Updating claim status with claim id: {}", claimId);
        User provider = userRepository.findById(userId)
                .orElseThrow(() ->
                        new DataNotFoundException("User not found with id: " + userId));
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new DataNotFoundException("Claim not found with id: " + claimId));
        ContractDetail contractDetail = contractDetailRepository.findByContractId(claim.getContractId())
                .orElseThrow(() ->
                        new DataNotFoundException("Contract detail not found with claim id: " + claimId));
        Product product = productRepository.findById(contractDetail.getProductId())
                .orElseThrow(() ->
                        new DataNotFoundException("Product not found with id: " + contractDetail.getProductId()));
        if (!product.getProvider().getId().equals(provider.getId())) {
            throw new DataNotFoundException("Claim not found with id: " + claimId);
        }
        claim.setStatus(status);
        claim.setModifiedBy(provider.getId());
        claim = claimRepository.save(claim);
        return claimMapper.fromEntityToDTO(claim);
    }
}
