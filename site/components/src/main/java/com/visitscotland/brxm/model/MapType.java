package com.visitscotland.brxm.model;

public enum MapType {
    REGIONAL("regional"),
    CITIES("cities"),
    GENERAL("general");

    private final String type;

    MapType(String mapType) {
        this.type = mapType;
    }

    public String getMapType() {
        return type;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
