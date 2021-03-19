package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoBean;

@HippoEssentialsGenerated(internalName = "visitscotland:MegalinkItem")
@Node(jcrType = "visitscotland:MegalinkItem")
public class MegalinkItem extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:feature")
    public Boolean getFeature() {
        return getSingleProperty("visitscotland:feature");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:link")
    public HippoBean getLink() {
        return getLinkedBean("visitscotland:link", HippoBean.class);
    }
}
