package com.visitscotland.brxm.model.navigation;

import com.visitscotland.brxm.hippobeans.FeaturedWidget;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;

import java.util.List;

public class FeaturedItem extends Module<FeaturedWidget> implements NavigationWidget {

    private List<EnhancedLink> links;

    public List<EnhancedLink> getLinks() {
        return links;
    }

    public void setLinks(List<EnhancedLink> links) {
        this.links = links;
    }
}
