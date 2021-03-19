package com.visitscotland.brxm.hippobeans;

import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.hippobeans.capabilities.TranslationParent;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "visitscotland:Page")
@Node(jcrType = "visitscotland:Page")
public class Page extends BaseDocument implements TranslationParent, Linkable {
    @HippoEssentialsGenerated(internalName = "visitscotland:seoTitle")
    public String getSeoTitle() {
        return getSingleProperty("visitscotland:seoTitle");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:breadcrumb")
    public String getBreadcrumb() {
        return getSingleProperty("visitscotland:breadcrumb");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:heroImage")
    public Image getHeroImage() {
        return getLinkedBean("visitscotland:heroImage", Image.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("visitscotland:introduction");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:seoNoIndex")
    public Boolean getSeoNoIndex() {
        return getSingleProperty("visitscotland:seoNoIndex");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:seoDescription")
    public String getSeoDescription() {
        return getSingleProperty("visitscotland:seoDescription");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:teaser")
    public String getTeaser() {
        return getSingleProperty("visitscotland:teaser");
    }

    public Image getImage() {
        return getHeroImage();
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:diff")
    public String getDiff() {
        return getSingleProperty("visitscotland:diff");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationFlag")
    public Boolean getTranslationFlag() {
        return getSingleProperty("visitscotland:translationFlag");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:otherThings")
    public OTYML getOtherThings() {
        return getBean("visitscotland:otherThings", OTYML.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }
}
