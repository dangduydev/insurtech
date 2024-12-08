package com.project.insurtech.enums;

public enum UserStatusEnum {
    ACTIVE(1),
    LOCKED(0);

    private final int value;

    UserStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
