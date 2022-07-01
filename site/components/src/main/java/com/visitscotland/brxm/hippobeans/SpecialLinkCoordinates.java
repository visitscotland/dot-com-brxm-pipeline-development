package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import com.visitscotland.brxm.hippobeans.Coordinates;

@HippoEssentialsGenerated(internalName = "visitscotland:SpecialLinkCoordinates")
@Node(jcrType = "visitscotland:SpecialLinkCoordinates")
public class SpecialLinkCoordinates extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:link")
    public HippoBean getLink() {
        return getLinkedBean("visitscotland:link", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:coordinates")
    public Coordinates getCoordinates() {
        return getBean("visitscotland:coordinates", Coordinates.class);
    }
}
