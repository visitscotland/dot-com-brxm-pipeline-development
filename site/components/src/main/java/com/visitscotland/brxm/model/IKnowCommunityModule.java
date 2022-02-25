package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.IknowCommunity;
import com.visitscotland.brxm.utils.HippoHtmlWrapper;

import java.util.List;

public class IKnowCommunityModule extends Module<IknowCommunity> {

    private String title;
    private HippoHtmlWrapper copy;
    private FlatLink link;
    private List<FlatLink> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HippoHtmlWrapper getCopy() {
        return copy;
    }

    public void setCopy(HippoHtmlWrapper copy) {
        this.copy = copy;
    }

    public List<FlatLink> getTags() {
        return tags;
    }

    public void setTags(List<FlatLink> tags) {
        this.tags = tags;
    }

    public FlatLink getLink() {
        return link;
    }

    public void setLink(FlatLink link) {
        this.link = link;
    }
}
