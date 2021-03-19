package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:Coordinates")
@Node(jcrType = "visitscotland:Coordinates")
public class Coordinates extends HippoCompound {
    public static final String LATITUDE = "visitscotland:latitude";
    public static final String LONGITUDE = "visitscotland:longitude";

    @HippoEssentialsGenerated(internalName = "visitscotland:latitude")
    public Double getLatitude() {
        return getSingleProperty(LATITUDE);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:longitude")
    public Double getLongitude() {
        return getSingleProperty(LONGITUDE);
    }
}
