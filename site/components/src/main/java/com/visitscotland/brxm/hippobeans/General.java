package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "visitscotland:General")
@Node(jcrType = "visitscotland:General")
public class General extends Page {
    @HippoEssentialsGenerated(internalName = "visitscotland:theme")
    public String getTheme() {
        return getSingleProperty("visitscotland:theme");
    }

    @Override
    public String[] getChildJcrTypes() {
        return new String[] { "visitscotland:Megalinks",
                "visitscotland:TourismInformation",
                "visitscotland:LongContent",
                "visitscotland:LongCopy",
                "visitscotland:Article"};
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:otherThings")
    public OTYML getOtherThings() {
        return getBean("visitscotland:otherThings", OTYML.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }

}
