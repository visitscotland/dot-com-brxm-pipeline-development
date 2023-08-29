package com.visitscotland.brxm.model;

public enum MapType {
    REGIONAL("regional"),
    CITIES("cities"),
    GENERAL("general");

    private final String mapType;

    MapType(String mapType) {
        this.mapType = mapType;
    }

    public String getMapType() {
        return mapType;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
