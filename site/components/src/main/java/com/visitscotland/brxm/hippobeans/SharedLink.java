package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import java.util.List;
import java.util.Calendar;
import com.visitscotland.brxm.hippobeans.Image;

@HippoEssentialsGenerated(internalName = "visitscotland:SharedLink")
@Node(jcrType = "visitscotland:SharedLink")
public class SharedLink extends SharedLinkBase {
    public static final String LINK_TYPES = "visitscotland:linkTypes";

    @HippoEssentialsGenerated(internalName = "visitscotland:diff")
    public String getDiff() {
        return getSingleProperty("visitscotland:diff");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationFlag")
    public Boolean getTranslationFlag() {
        return getSingleProperty("visitscotland:translationFlag");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationPriority")
    public String getTranslationPriority() {
        return getSingleProperty("visitscotland:translationPriority");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }

}
