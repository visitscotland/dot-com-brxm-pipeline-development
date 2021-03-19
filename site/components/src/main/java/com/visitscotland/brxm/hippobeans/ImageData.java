package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:ImageData")
@Node(jcrType = "visitscotland:ImageData")
public class ImageData extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:caption")
    public String getCaption() {
        return getSingleProperty("visitscotland:caption");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:altText")
    public String getAltText() {
        return getSingleProperty("visitscotland:altText");
    }
}
