package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import java.util.Calendar;

/** 
 * TODO: Beanwriter: Failed to create getter for node type: hippo:compound
 */
@HippoEssentialsGenerated(internalName = "visitscotland:Banner")
@Node(jcrType = "visitscotland:Banner")
public class Banner extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:copy")
    public HippoHtml getCopy() {
        return getHippoHtml("visitscotland:copy");
    }

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

    public HippoBean getCtaLink() {
        return getBean("visitscotland:ctaLink");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }
}
