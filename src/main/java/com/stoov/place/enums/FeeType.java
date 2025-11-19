package com.stoov.place.enums;

public enum FeeType {
    FREE("무료"),
    PAID("유료"),
    UNKNOWN("정보 없음");

    private String value;
    FeeType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
