package com.visitscotland.brxm.model;

public enum LinkType {
    INTERNAL, EXTERNAL, DOWNLOAD;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
