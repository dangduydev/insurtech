package com.project.insurtech.enums;

public enum ClaimStatusEnum {
    PENDING(0),
    APPROVED(1),
    REJECTED(2);

    private final int value;

    ClaimStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
