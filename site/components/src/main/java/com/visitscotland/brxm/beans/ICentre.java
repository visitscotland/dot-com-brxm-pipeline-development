package com.visitscotland.brxm.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "visitscotland:ICentre")
@Node(jcrType = "visitscotland:ICentre")
public class ICentre extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:image")
    public Image getImage() {
        return getLinkedBean("visitscotland:image", Image.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:quotes", allowModifications = false)
    public List<Quote> getQuotes() {
        return getChildBeansByName("visitscotland:quotes", Quote.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:quote")
    public Quote getQuote() {
        return getBean("visitscotland:quote", Quote.class);
    }
}
