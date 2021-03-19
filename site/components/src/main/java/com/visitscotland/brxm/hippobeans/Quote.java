package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoBean;

/** 
 * TODO: Beanwriter: Failed to create getter for node type: hippo:compound
 */
@HippoEssentialsGenerated(internalName = "visitscotland:Quote")
@Node(jcrType = "visitscotland:Quote")
public class Quote extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:image")
    public Image getImage() {
        return getLinkedBean("visitscotland:image", Image.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:author")
    public String getAuthor() {
        return getSingleProperty("visitscotland:author");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:quote")
    public HippoHtml getQuote() {
        return getHippoHtml("visitscotland:quote");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:role")
    public String getRole() {
        return getSingleProperty("visitscotland:role");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:product")
    public HippoBean getProduct() {
        return getLinkedBean("visitscotland:product", HippoBean.class);
    }
}
