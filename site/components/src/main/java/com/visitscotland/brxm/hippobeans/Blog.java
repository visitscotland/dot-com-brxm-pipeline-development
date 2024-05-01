package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import java.util.Calendar;
import org.hippoecm.hst.content.beans.standard.HippoBean;

@HippoEssentialsGenerated(internalName = "visitscotland:Blog")
@Node(jcrType = "visitscotland:Blog")
public class Blog extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:readingTime")
    public Long getReadingTime() {
        return getSingleProperty("visitscotland:readingTime");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:publishDate")
    public Calendar getPublishDate() {
        return getSingleProperty("visitscotland:publishDate");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:author")
    public HippoBean getAuthor() {
        return getLinkedBean("visitscotland:author", HippoBean.class);
    }
}
