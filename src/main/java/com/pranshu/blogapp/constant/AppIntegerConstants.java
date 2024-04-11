package com.pranshu.blogapp.constant;

public enum AppIntegerConstants {
    PAGE_SIZE(10);

    private final int value;

    AppIntegerConstants(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}


