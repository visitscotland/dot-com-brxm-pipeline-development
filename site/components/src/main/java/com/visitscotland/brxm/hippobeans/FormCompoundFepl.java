package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:FormCompoundFepl")
@Node(jcrType = "visitscotland:FormCompoundFepl")
public class FormCompoundFepl extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:url")
    public String getUrl() {
        return getSingleProperty("visitscotland:url");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:jsonURL")
    public String getJsonURL() {
        return getSingleProperty("visitscotland:jsonURL");
    }
}
