package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import java.util.List;
import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "visitscotland:FeaturedWidget")
@Node(jcrType = "visitscotland:FeaturedWidget")
public class FeaturedWidget extends BaseDocument  {
    @HippoEssentialsGenerated(internalName = "visitscotland:items", allowModifications = false)
    public List<HippoBean> getItems() {
        return getChildBeansByName("visitscotland:items");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }
}
