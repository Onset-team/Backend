package com.stoov.place.enums;

public enum MaxPerformersType {
    LIMITED("제한 있음"),
    UNLIMITED("제한 없음"),
    UNKNOWN("정보 없음");

    private String value;
    MaxPerformersType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
