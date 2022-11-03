package com.visitscotland.brxm.model;

public enum LinkType {
    INTERNAL("default"),
    MAIL("default"),
    EXTERNAL("external"),
    DOWNLOAD("download"),
    VIDEO("video");

    private final String richTextType;

    LinkType(String richTextType) {
        this.richTextType = richTextType;
    }

    public String getRichTextType() {
        return richTextType;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
