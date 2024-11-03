package com.project.insurtech.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE("MALE"),

    FEMALE("FEMALE"),
    ALL("ALL");

    private final String value;

    GenderEnum(String value) {
        this.value = value;
    }
}
