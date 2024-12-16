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

    public static GenderEnum fromValue(String value) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender value: " + value);
    }

}
