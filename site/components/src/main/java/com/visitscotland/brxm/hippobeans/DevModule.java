package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

@HippoEssentialsGenerated(internalName = "visitscotland:DevModule")
@Node(jcrType = "visitscotland:DevModule")
public class DevModule extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:html")
    public String getHtml() {
        return getSingleProperty("visitscotland:html");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:notes")
    public String getNotes() {
        return getSingleProperty("visitscotland:notes");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:headContributions")
    public String[] getHeadContributions() {
        return getMultipleProperty("visitscotland:headContributions");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:footerContributions")
    public String[] getFooterContributions() {
        return getMultipleProperty("visitscotland:footerContributions");
    }
}
