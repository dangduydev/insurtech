package com.project.insurtech.enums;

public enum RoleEnum {
    ADMIN(1),
    USER(2),
    PROVIDER(3),
    ;

    private final Integer value;

    RoleEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
