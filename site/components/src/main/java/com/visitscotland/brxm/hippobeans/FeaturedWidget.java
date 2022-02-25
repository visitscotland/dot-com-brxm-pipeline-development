package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import java.util.List;
import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "visitscotland:FeaturedWidget")
@Node(jcrType = "visitscotland:FeaturedWidget")
public class FeaturedWidget extends Widget {
    @HippoEssentialsGenerated(internalName = "visitscotland:items", allowModifications = false)
    public List<CMSLink> getItems() {
        return getChildBeans(CMSLink.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }
}
