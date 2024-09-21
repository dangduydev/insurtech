package com.project.insurtech.common;

import lombok.Getter;


public class StaticEnum {
    @Getter
    public enum CourseStatus {
        INIT("INIT", "Init"),
        ACTIVE("ACTIVE", "Active");


        private final String code;

        CourseStatus(String code, String description) {
            this.code = code;
        }
    }
}
