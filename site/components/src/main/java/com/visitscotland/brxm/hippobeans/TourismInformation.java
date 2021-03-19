package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "visitscotland:TourismInformation")
@Node(jcrType = "visitscotland:TourismInformation")
public class TourismInformation extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:ICentre")
    public ICentre getICentre() {
        return getBean("visitscotland:ICentre", ICentre.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:IKnow")
    public IKnow getIKnow() {
        return getBean("visitscotland:IKnow", IKnow.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:diff")
    public String getDiff() {
        return getSingleProperty("visitscotland:diff");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationFlag")
    public Boolean getTranslationFlag() {
        return getSingleProperty("visitscotland:translationFlag");
    }
}
