package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "visitscotland:VideoLink")
@Node(jcrType = "visitscotland:VideoLink")
public class VideoLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:videoLink")
    public Video getVideoLink() {
        return getLinkedBean("visitscotland:videoLink", Video.class);
    }
}
