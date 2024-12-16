package com.project.insurtech.enums;

public enum ContractStatusEnum {
    PENDING(0),
    ACTIVE(1),
    EXPIRED(2),
    CANCELLED(3);

    private final int value;

    ContractStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
