package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import java.util.List;
import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "visitscotland:Megalinks")
@Node(jcrType = "visitscotland:Megalinks")
public class Megalinks extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("visitscotland:introduction");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:singleImageLinks", allowModifications = false)
    public List<SingleImageModule> getSingleImageLinks() {
        return getChildBeansByName("visitscotland:singleImageLinks",
                SingleImageModule.class);
    }

    public SingleImageModule getSingleImageModule() {
        return getOnlyChild(getSingleImageLinks());
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:megalinkItems", allowModifications = false)
    public List<MegalinkItem> getMegalinkItems() {
        return getChildBeansByName("visitscotland:megalinkItems",
                MegalinkItem.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:teaserVisible")
    public Boolean getTeaserVisible() {
        return getSingleProperty("visitscotland:teaserVisible");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:products", allowModifications = false)
    public List<HippoCompound> getProducts() {
        return getChildBeansByName("visitscotland:products",
                HippoCompound.class);
    }

    public HippoCompound getProductItem() {
        return getOnlyChild(getProducts());
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:diff")
    public String getDiff() {
        return getSingleProperty("visitscotland:diff");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationFlag")
    public Boolean getTranslationFlag() {
        return getSingleProperty("visitscotland:translationFlag");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:layout")
    public String getLayout() {
        return getSingleProperty("visitscotland:layout");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationPriority")
    public String getTranslationPriority() {
        return getSingleProperty("visitscotland:translationPriority");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:personalization")
    public List<Personalization> getPersonalization() {
        return getChildBeansByName("visitscotland:personalization",
                Personalization.class);
    }
}
