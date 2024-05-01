package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;

@HippoEssentialsGenerated(internalName = "visitscotland:Profile")
@Node(jcrType = "visitscotland:Profile")
public class Profile extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:name")
    public String getName() {
        return getSingleProperty("visitscotland:name");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:photo")
    public HippoGalleryImageSet getPhoto() {
        return getLinkedBean("visitscotland:photo", HippoGalleryImageSet.class);
    }
}
