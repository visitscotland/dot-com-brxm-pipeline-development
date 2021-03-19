package com.visitscotland.brxm.model;

public class IKnowModule extends Module{

    private String title;
    private String description;
    private FlatLink link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FlatLink getLink() {
        return link;
    }

    public void setLink(FlatLink link) {
        this.link = link;
    }
}
