package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.hippobeans.capabilities.UrlLink;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:ExternalLink")
@Node(jcrType = "visitscotland:ExternalLink")
public class ExternalLink extends HippoCompound implements UrlLink {
    @HippoEssentialsGenerated(internalName = "visitscotland:link")
    public String getLink() {
        return getSingleProperty("visitscotland:link");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:label")
    public String getLabel() {
        return getSingleProperty("visitscotland:label");
    }
}
