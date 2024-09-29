package com.project.insurtech.service;

import com.project.insurtech.entities.ProductType;
import com.project.insurtech.repository.IProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final IProductTypeRepository productTypeRepository;

    @Override
    public List<ProductType> getProductTypes() {
        return productTypeRepository.findAll();
    }


}
