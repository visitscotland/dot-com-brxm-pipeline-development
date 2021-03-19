package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:DMSLink")
@Node(jcrType = "visitscotland:DMSLink")
public class DMSLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:product")
    public String getProduct() {
        return getSingleProperty("visitscotland:product");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:label")
    public String getLabel() {
        return getSingleProperty("visitscotland:label");
    }
}
