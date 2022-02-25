package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;

@HippoEssentialsGenerated(internalName = "visitscotland:Image")
@Node(jcrType = "visitscotland:Image")
public class Image extends HippoGalleryImageSet {
    public static final String ALT_TEXT = "visitscotland:altText";
    public static final String CREDIT = "visitscotland:credit";

    @HippoEssentialsGenerated(internalName = "visitscotland:altText")
    public String getAltText() {
        return getSingleProperty("visitscotland:altText");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:credit")
    public String getCredit() {
        return getSingleProperty("visitscotland:credit");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:location")
    public String getLocation() {
        return getSingleProperty("visitscotland:location");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:de")
    public ImageData getDe() {
        return getBean("visitscotland:de", ImageData.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:es")
    public ImageData getEs() {
        return getBean("visitscotland:es", ImageData.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:fr")
    public ImageData getFr() {
        return getBean("visitscotland:fr", ImageData.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:it")
    public ImageData getIt() {
        return getBean("visitscotland:it", ImageData.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:nl")
    public ImageData getNl() {
        return getBean("visitscotland:nl", ImageData.class);
    }
}
