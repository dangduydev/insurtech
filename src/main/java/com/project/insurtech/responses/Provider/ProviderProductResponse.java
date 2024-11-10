package com.project.insurtech.responses.Provider;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderProductResponse {
    private Long providerId;
    private String providerName;
    private Long countProduct;
}
