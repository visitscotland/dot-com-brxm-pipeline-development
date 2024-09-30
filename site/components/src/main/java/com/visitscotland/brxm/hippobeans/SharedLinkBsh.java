package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

@HippoEssentialsGenerated(internalName = "visitscotland:SharedLinkBsh")
@Node(jcrType = "visitscotland:SharedLinkBsh")
public class SharedLinkBsh extends SharedLinkBase {
    @HippoEssentialsGenerated(internalName = "visitscotland:source")
    public String getSource() {
        return getSingleProperty("visitscotland:source");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:sectors")
    public String[] getSectors() {
        return getMultipleProperty("visitscotland:sectors");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:types")
    public String[] getTypes() {
        return getMultipleProperty("visitscotland:types");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:skill")
    public String getSkill() {
        return getSingleProperty("visitscotland:skill");
    }
}
