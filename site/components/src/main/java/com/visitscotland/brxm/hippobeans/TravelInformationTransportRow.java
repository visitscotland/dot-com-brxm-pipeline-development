package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@HippoEssentialsGenerated(internalName = "visitscotland:TravelInformationTransportRow")
@Node(jcrType = "visitscotland:TravelInformationTransportRow")
public class TravelInformationTransportRow extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:transport")
    public String getTransport() {
        return getSingleProperty("visitscotland:transport");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:copy")
    public HippoHtml getCopy() {
        return getHippoHtml("visitscotland:copy");
    }
}
