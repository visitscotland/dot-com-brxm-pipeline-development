package com.visitscotland.brxm.model.megalinks;

import java.util.List;

public class MultiImageLinksModule extends LinksModule<EnhancedLink> {

    private boolean teaserVisible;
    private List<EnhancedLink> featuredLinks;
    private Integer linksSize;

    public MultiImageLinksModule() {

    }

    public MultiImageLinksModule(String title) {
        setTitle(title);
    }

    public boolean isTeaserVisible() {
        return teaserVisible;
    }

    public void setTeaserVisible(boolean teaserVisible) {
        this.teaserVisible = teaserVisible;
    }

    public List<EnhancedLink> getFeaturedLinks() {
        return featuredLinks;
    }

    public Integer getLinksSize() {
        return linksSize;
    }

    public void setLinksSize(Integer linksSize) {
        this.linksSize = linksSize;
    }


    public void setFeaturedLinks(List<EnhancedLink> featuredLinks) {
        this.featuredLinks = featuredLinks;
    }

}
