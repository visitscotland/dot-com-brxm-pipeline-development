package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.hippobeans.capabilities.UrlLink;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:ExternalDocument")
@Node(jcrType = "visitscotland:ExternalDocument")
public class ExternalDocument extends HippoCompound implements UrlLink {
    @HippoEssentialsGenerated(internalName = "visitscotland:category")
    public String getCategory() {
        return getSingleProperty("visitscotland:category");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:link")
    public String getLink() {
        return getSingleProperty("visitscotland:link");
    }
}
