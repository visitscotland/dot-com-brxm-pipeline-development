package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoBean;

@HippoEssentialsGenerated(internalName = "visitscotland:CMSLink")
@Node(jcrType = "visitscotland:CMSLink")
public class CMSLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:label")
    public String getLabel() {
        return getSingleProperty("visitscotland:label");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:link")
    public HippoBean getLink() {
        return getLinkedBean("visitscotland:link", HippoBean.class);
    }
}
