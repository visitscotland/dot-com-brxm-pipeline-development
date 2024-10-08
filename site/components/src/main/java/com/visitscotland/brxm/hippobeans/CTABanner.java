package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import com.visitscotland.brxm.hippobeans.Image;
import com.visitscotland.brxm.hippobeans.CMSLink;

@HippoEssentialsGenerated(internalName = "visitscotland:CTABanner")
@Node(jcrType = "visitscotland:CTABanner")
public class CTABanner extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:nested")
    public Boolean getNested() {
        return getSingleProperty("visitscotland:nested");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("visitscotland:introduction");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:image")
    public Image getImage() {
        return getLinkedBean("visitscotland:image", Image.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:ctaLink")
    public CMSLink getCtaLink() {
        return getBean("visitscotland:ctaLink", CMSLink.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:anchor")
    public String getAnchor() {
        return getSingleProperty("visitscotland:anchor");
    }
}
