package com.visitscotland.brxm.model;

public class FlatLink {

    private String label;
    private String link;
    private LinkType type;

    public FlatLink() {

    }

    public FlatLink(String label, String link, LinkType type) {
        this.label = label;
        this.link = link;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LinkType getType() {
        return type;
    }

    public void setType(LinkType type) {
        this.type = type;
    }
}
