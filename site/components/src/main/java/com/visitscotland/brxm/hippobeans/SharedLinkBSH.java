package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

@HippoEssentialsGenerated(internalName = "visitscotland:SharedLinkBSH")
@Node(jcrType = "visitscotland:SharedLinkBSH")
public class SharedLinkBSH extends SharedLink {
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

    @HippoEssentialsGenerated(internalName = "visitscotland:regions")
    public String[] getRegions() {
        return getMultipleProperty("visitscotland:regions");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:type")
    public String getType() {
        return getSingleProperty("visitscotland:type");
    }
}
