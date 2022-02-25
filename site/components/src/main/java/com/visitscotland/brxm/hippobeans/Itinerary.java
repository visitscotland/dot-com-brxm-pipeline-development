package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "visitscotland:Itinerary")
@Node(jcrType = "visitscotland:Itinerary")
public class Itinerary extends Page {
    @HippoEssentialsGenerated(internalName = "visitscotland:start")
    public String getStart() {
        return getSingleProperty("visitscotland:start");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:finish")
    public String getFinish() {
        return getSingleProperty("visitscotland:finish");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:distance")
    public Double getDistance() {
        return getSingleProperty("visitscotland:distance");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:theme")
    public String getTheme() {
        return getSingleProperty("visitscotland:theme");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:areas")
    public String[] getAreas() {
        return getMultipleProperty("visitscotland:areas");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:transports")
    public String[] getTransports() {
        return getMultipleProperty("visitscotland:transports");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:popular")
    public Boolean getPopular() {
        return getSingleProperty("visitscotland:popular");
    }

    @Override
    public String[] getChildJcrTypes() {
        return new String[] { "visitscotland:Day" };
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:otherThings")
    public com.visitscotland.brxm.hippobeans.OTYML getOtherThings() {
        return getBean("visitscotland:otherThings",
                com.visitscotland.brxm.hippobeans.OTYML.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:highlights")
    public String[] getHighlights() {
        return getMultipleProperty("visitscotland:highlights");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationPriority")
    public String getTranslationPriority() {
        return getSingleProperty("visitscotland:translationPriority");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:heroVideo")
    public VideoLink getHeroVideo() {
        return getBean("visitscotland:heroVideo", VideoLink.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }
}
