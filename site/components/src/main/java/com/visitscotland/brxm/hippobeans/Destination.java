package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "visitscotland:Destination")
@Node(jcrType = "visitscotland:Destination")
public class Destination extends Page {
    @HippoEssentialsGenerated(internalName = "visitscotland:location")
    public String getLocation() {
        return getSingleProperty("visitscotland:location");
    }

    @Override
    public String[] getChildJcrTypes() {
        return new String[] { "visitscotland:Megalinks",
                "visitscotland:TourismInformation" };
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:otherThings")
    public OTYML getOtherThings() {
        return getBean("visitscotland:otherThings", OTYML.class);
    }
}
