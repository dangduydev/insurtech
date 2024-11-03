package com.project.insurtech.enums;

public enum IsDeletedEnum {
    DELETED(true),
    NOT_DELETED(false);

    private final Boolean value;

    IsDeletedEnum(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
