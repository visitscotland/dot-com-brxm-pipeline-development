package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

@HippoEssentialsGenerated(internalName = "visitscotland:ArticleBSH")
@Node(jcrType = "visitscotland:ArticleBSH")
public class ArticleBSH extends Article {
    @HippoEssentialsGenerated(internalName = "visitscotland:nested")
    public Boolean getNested() {
        return getSingleProperty("visitscotland:nested");
    }
}
