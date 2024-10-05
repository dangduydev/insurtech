package com.project.insurtech.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {
    AVAILABLE("AVAILABLE"),         // Product is available for purchase
    COMING_SOON("COMING_SOON"),     // customer can see it but cannot buy it
    DISCONTINUED("DISCONTINUED"),   // customer cannot see it
    DRAFT("DRAFT");                 // customer cannot see it

    private final String value;

    ProductStatusEnum(String value) {
        this.value = value;
    }
}

