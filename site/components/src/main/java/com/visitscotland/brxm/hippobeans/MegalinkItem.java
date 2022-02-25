package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoMirror;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import java.util.List;

@HippoEssentialsGenerated(internalName = "visitscotland:MegalinkItem")
@Node(jcrType = "visitscotland:MegalinkItem")
public class MegalinkItem extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:feature")
    public Boolean getFeature() {
        return getSingleProperty("visitscotland:feature");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:links", allowModifications = false)
    public HippoBean getLinkItem() {
        if (getBean("visitscotland:links") instanceof HippoMirror) {
            return getLinkedBean("visitscotland:links", HippoBean.class);
        } else {
            return getBean("visitscotland:links");
        }
    }
}
