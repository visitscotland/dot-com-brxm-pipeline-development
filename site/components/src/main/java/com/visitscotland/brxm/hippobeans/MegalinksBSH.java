package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import com.visitscotland.brxm.hippobeans.CMSLink;

@HippoEssentialsGenerated(internalName = "visitscotland:MegalinksBSH")
@Node(jcrType = "visitscotland:MegalinksBSH")
public class MegalinksBSH extends Megalinks {
    @HippoEssentialsGenerated(internalName = "visitscotland:anchor")
    public String getAnchor() {
        return getSingleProperty("visitscotland:anchor");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:nested")
    public Boolean getNested() {
        return getSingleProperty("visitscotland:nested");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:productsCMS")
    public CMSLink getProductsCMS() {
        return getBean("visitscotland:productsCMS", CMSLink.class);
    }
}
