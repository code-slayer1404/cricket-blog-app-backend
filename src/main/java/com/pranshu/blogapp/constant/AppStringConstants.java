package com.pranshu.blogapp.constant;

public enum AppStringConstants {
    USER("USER"),
    ADMIN("ADMIN"),
    ROLE("ROLE");

    private final String value;

    AppStringConstants(String i) {
        this.value = i;
    }

    public String getValue() {
        return value;
    }
}
