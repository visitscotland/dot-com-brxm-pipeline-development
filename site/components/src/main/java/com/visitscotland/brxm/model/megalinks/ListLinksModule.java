package com.visitscotland.brxm.model.megalinks;

public class ListLinksModule extends LinksModule<EnhancedLink> {

    private boolean teaserVisible;

    public boolean isTeaserVisible() {
        return teaserVisible;
    }

    public void setTeaserVisible(boolean teaserVisible) {
        this.teaserVisible = teaserVisible;
    }
}
