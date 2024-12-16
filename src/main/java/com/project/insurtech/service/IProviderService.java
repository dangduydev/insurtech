package com.project.insurtech.service;

import com.project.insurtech.responses.Provider.ProviderProductResponse;

import java.util.List;

public interface IProviderService {

    List<ProviderProductResponse> getProviderProduct();
}
