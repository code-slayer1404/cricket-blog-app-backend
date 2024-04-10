package com.pranshu.blogapp.constant;

public enum PageSize {
    PAGE_SIZE(10);

    private final int value;

    PageSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}