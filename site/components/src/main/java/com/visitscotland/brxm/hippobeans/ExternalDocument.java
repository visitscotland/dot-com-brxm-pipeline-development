package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:ExternalDocument")
@Node(jcrType = "visitscotland:ExternalDocument")
public class ExternalDocument extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:category")
    public String getCategory() {
        return getSingleProperty("visitscotland:category");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:size")
    public Double getSize() {
        return getSingleProperty("visitscotland:size");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:bytes")
    public String getBytes() {
        return getSingleProperty("visitscotland:bytes");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:link")
    public String getLink() {
        return getSingleProperty("visitscotland:link");
    }
}
