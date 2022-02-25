package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "visitscotland:Listicle")
@Node(jcrType = "visitscotland:Listicle")
public class Listicle extends Page {
    @HippoEssentialsGenerated(internalName = "visitscotland:descOrder")
    public Boolean getDescOrder() {
        return getSingleProperty("visitscotland:descOrder");
    }

    @Override
    public String[] getChildJcrTypes() {
        return new String[] { "visitscotland:ListicleItem" };
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:ListicleClosing")
    public ListicleClosing getListicleClosing() {
        return getBean("visitscotland:ListicleClosing", ListicleClosing.class);
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
