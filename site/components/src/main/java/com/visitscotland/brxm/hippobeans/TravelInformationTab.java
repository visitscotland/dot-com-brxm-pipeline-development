package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

import java.util.List;

@HippoEssentialsGenerated(internalName = "visitscotland:TravelInformationTab")
@Node(jcrType = "visitscotland:TravelInformationTab")
public class TravelInformationTab extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:accordion", allowModifications = false)
    public List<TravelInformationTransportRow> getAccordion() {
        return getChildBeansByName("visitscotland:accordion",
                TravelInformationTransportRow.class);
    }
}
