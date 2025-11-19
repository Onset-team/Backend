package com.stoov.place.enums;

public enum PlaceType {
    RIVER_PARK("강변/공원"),
    SQUARE("광장"),
    STREET("거리");

    private String value;
    PlaceType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
