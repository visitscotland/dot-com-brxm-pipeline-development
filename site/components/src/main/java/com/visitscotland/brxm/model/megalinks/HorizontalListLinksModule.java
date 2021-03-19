package com.visitscotland.brxm.model.megalinks;

public class HorizontalListLinksModule extends LinksModule<EnhancedLink> {

    private boolean teaserVisible;
    private String category;

    public boolean isTeaserVisible() {
        return teaserVisible;
    }

    public void setTeaserVisible(boolean teaserVisible) {
        this.teaserVisible = teaserVisible;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}