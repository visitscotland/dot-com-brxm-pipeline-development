package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.model.form.FeplConfiguration;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

/**
 * TODO: Beanwriter: Failed to create getter for node type: hippo:compound
 */
@HippoEssentialsGenerated(internalName = "visitscotland:Form")
@Node(jcrType = "visitscotland:Form")
public class Form extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:nonJavaScriptMessage")
    public String getNonJavaScriptMessage() {
        return getSingleProperty("visitscotland:nonJavaScriptMessage");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:copy")
    public HippoHtml getCopy() {
        return getHippoHtml("visitscotland:copy");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:form", allowModifications = false)
    public HippoCompound getFormConfiguration() {
        return getBean("visitscotland:form", HippoCompound.class);
    }
}
