package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:FormCompoundBreg")
@Node(jcrType = "visitscotland:FormCompoundBreg")
public class FormCompoundBreg extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:jsonUrl")
    public String getJsonUrl() {
        return getSingleProperty("visitscotland:jsonUrl");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:url")
    public String getUrl() {
        return getSingleProperty("visitscotland:url");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:activityCode")
    public String getActivityCode() {
        return getSingleProperty("visitscotland:activityCode");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:activityDescription")
    public String getActivityDescription() {
        return getSingleProperty("visitscotland:activityDescription");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:activitySource")
    public String getActivitySource() {
        return getSingleProperty("visitscotland:activitySource");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:consents")
    public String[] getConsents() {
        return getMultipleProperty("visitscotland:consents");
    }
}
