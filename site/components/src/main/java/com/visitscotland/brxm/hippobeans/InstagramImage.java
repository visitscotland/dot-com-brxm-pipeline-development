package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:InstagramImage")
@Node(jcrType = "visitscotland:InstagramImage")
public class InstagramImage extends HippoCompound {
    public static final String CAPTION = "visitscotland:caption";

    @HippoEssentialsGenerated(internalName = "visitscotland:caption")
    public String getCaption() {
        return getSingleProperty("visitscotland:caption");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:id")
    public String getId() {
        return getSingleProperty("visitscotland:id");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:location")
    public String getLocation() {
        return getSingleProperty("visitscotland:location");
    }
}
