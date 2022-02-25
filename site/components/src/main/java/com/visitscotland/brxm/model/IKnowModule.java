package com.visitscotland.brxm.model;

import com.visitscotland.brxm.utils.HippoHtmlWrapper;

public class IKnowModule extends Module{

    private String title;
    private HippoHtmlWrapper description;
    private FlatLink link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HippoHtmlWrapper getDescription() {
        return description;
    }

    public void setDescription(HippoHtmlWrapper description) {
        this.description = description;
    }

    public FlatLink getLink() {
        return link;
    }

    public void setLink(FlatLink link) {
        this.link = link;
    }
}
