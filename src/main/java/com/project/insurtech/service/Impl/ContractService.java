package com.project.insurtech.service.Impl;

import com.project.insurtech.components.mappers.ContractMapper;
import com.project.insurtech.dtos.ContractDTO;
import com.project.insurtech.dtos.SideTermDTO;
import com.project.insurtech.entities.*;
import com.project.insurtech.enums.ContractStatusEnum;
import com.project.insurtech.enums.GenderEnum;
import com.project.insurtech.exceptions.DataNotFoundException;
import com.project.insurtech.repositories.*;
import com.project.insurtech.responses.Contract.ContractListResponse;
import com.project.insurtech.responses.Contract.UserGetContractListResponse;
import com.project.insurtech.service.IContractService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService implements IContractService {
    private final Logger logger = LoggerFactory.getLogger(ContractService.class);
    private final ContractMapper contractMapper;
    private final IContractRepository contractRepository;
    private final IContractDetailRepository contractDetailRepository;
    private final IContractMainTermRepository contractMainTermRepository;
    private final IContractSideTermRepository contractSideTermRepository;
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IMainTermRepository mainTermRepository;
    private final ISideTermRepository sideTermRepository;
    private final IUserRepository userRepository;

    @Override
    @Transactional
    public Contract createContract(ContractDTO contractDTO, Long userId) throws DataNotFoundException {
        logger.info("Creating contract: {}", contractDTO);
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new DataNotFoundException("User not found with id: " + userId));
            Product product = productRepository.findById(contractDTO.getContractDetailDTO().getProductId())
                    .orElseThrow(() ->
                            new DataNotFoundException("Product not found with id: " +
                                    contractDTO.getContractDetailDTO().getProductId()));
            List<MainTerm> mainTerms = mainTermRepository.findAllById(
                    product.getMainTerms().stream().map(MainTerm::getId)
                            .collect(Collectors.toList())
            );
            List<SideTerm> sideTerms = sideTermRepository.findAllById(
                    contractDTO.getContractDetailDTO().getSideTerms().stream().map(SideTermDTO::getId)
                            .collect(Collectors.toList()));

            double price = product.getPrice() +
                    +sideTerms.stream().mapToDouble(SideTerm::getPrice).sum();
            Contract contract = new Contract().builder()
                    .status(ContractStatusEnum.ACTIVE.getValue())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusYears(1))  //Todo: Calculate end date based on product
                    .name(contractDTO.getName())
                    .dob(contractDTO.getDob())
                    .gender(GenderEnum.fromValue(contractDTO.getGender()).getValue())
                    .identification(contractDTO.getIdentification())
                    .phone(contractDTO.getPhone())
                    .email(contractDTO.getEmail())
                    .address(contractDTO.getAddress())
                    .price(price)
                    .createdBy(userId)
                    .modifiedBy(userId)
                    .build();
            contract.setUser(user);
            contract = contractRepository.save(contract);

            ContractDetail contractDetail = new ContractDetail().builder()
                    .contract(contract)
                    .productName(product.getName())
                    .productDescription(product.getDescription())
                    .applicableObject(product.getApplicableObject())
                    .scope(product.getScope())
                    .exclusion(product.getExclusion())
                    .name(contractDTO.getContractDetailDTO().getName())
                    .dob(contractDTO.getContractDetailDTO().getDob())
                    .gender(GenderEnum.fromValue(contractDTO.getContractDetailDTO().getGender()).getValue())
                    .identification(contractDTO.getContractDetailDTO().getIdentification())
                    .phone(contractDTO.getContractDetailDTO().getPhone())
                    .email(contractDTO.getContractDetailDTO().getEmail())
                    .address(contractDTO.getContractDetailDTO().getAddress())
                    .createdBy(userId)
                    .modifiedBy(userId)
                    .build();
            contractDetail.setProductId(product.getId());
            contractDetailRepository.save(contractDetail);
            List<ContractMainTerm> contractMainTerms = new ArrayList<>();
            for (MainTerm mainTerm : mainTerms) {
                ContractMainTerm contractMainTerm = new ContractMainTerm().builder()
                        .contractDetail(contractDetail)
                        .name(mainTerm.getName())
                        .description(mainTerm.getDescription())
                        .amount(mainTerm.getAmount())
                        .createdBy(userId)
                        .modifiedBy(userId)
                        .build();
                contractMainTermRepository.save(contractMainTerm);
                contractMainTerms.add(contractMainTerm);
            }
            List<ContractSideTerm> contractSideTerms = new ArrayList<>();
            for (SideTerm sideTerm : sideTerms) {
                ContractSideTerm contractSideTerm = new ContractSideTerm().builder()
                        .contractDetail(contractDetail)
                        .name(sideTerm.getName())
                        .description(sideTerm.getDescription())
                        .amount(sideTerm.getAmount())
                        .price(sideTerm.getPrice())
                        .createdBy(userId)
                        .modifiedBy(userId)
                        .build();
                contractSideTermRepository.save(contractSideTerm);
                contractSideTerms.add(contractSideTerm);
            }
            return contract;
        } catch (Exception e) {
            logger.error("Failed to create contract: {}", e.getMessage());
            throw new DataNotFoundException("Failed to create contract due to server error");
        }
    }

    @Override
    public List<UserGetContractListResponse> getContractsByUserId(Long userId) throws DataNotFoundException {
        return contractRepository.findContractsByUserId(userId);
    }

    @Override
    public Contract getContractDetail(Long contractId) throws DataNotFoundException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new DataNotFoundException("Contract not found with id: " + contractId));
//        ContractDTO contractDTO = contractMapper.fromEntityToDTO(contract);
        ContractDetail contractDetail = contractDetailRepository.findByContractId(contractId)
                .orElseThrow(() -> new DataNotFoundException("Contract detail not found with contract id: " + contractId));
//        ContractDetailDTO contractDetailDTO = contractMapper.fromEntityToDTO(contractDetail);
//        contractDetailDTO.setContractId(contract.getId());
//        contractDetailDTO.setProductId(contractDetail.getProduct().getId());

        List<ContractMainTerm> contractMainTerms = contractMainTermRepository.findByContractDetailId(contractDetail.getId());
        List<ContractSideTerm> contractSideTerms = contractSideTermRepository.findByContractDetailId(contractDetail.getId());

//        contractDTO.setContractDetailDTO(contractDetailDTO);
//        contractDTO.setUserId(contract.getUser().getId());
        contractDetail.setContractMainTerms(contractMainTerms);
        contractDetail.setContractSideTerms(contractSideTerms);
        contract.setContractDetail(contractDetail);
        return contract;
    }

    @Override
    public Page<ContractListResponse> getContractsByProviderId(
            Integer status,
            String categoryName,
            String productName,
            String customerName,
            Long providerId,
            Pageable pageable
    ) {
        return contractRepository.findByProviderWithFilters(
                providerId, status, categoryName, productName, customerName, pageable);
    }
}