package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoMirror;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;
import java.util.stream.Collectors;

@HippoEssentialsGenerated(internalName = "visitscotland:ListicleItem")
@Node(jcrType = "visitscotland:ListicleItem")
public class ListicleItem extends BaseDocument {
    public static final String IMAGES = "visitscotland:images";
    public static final String PRODUCT = "visitscotland:product";

    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:subtitle")
    public String getSubtitle() {
        return getSingleProperty("visitscotland:subtitle");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:description")
    public HippoHtml getDescription() {
        return getHippoHtml("visitscotland:description");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:products", allowModifications = false)
    public List<HippoCompound> getProducts() {
        return getChildBeansByName("visitscotland:products",
                HippoCompound.class);
    }

    public HippoCompound getListicleItem() {
        return getOnlyChild(getProducts());
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:images", allowModifications = false)
    public List<HippoBean> getImages() {
        return getChildBeansByName("visitscotland:images", HippoBean.class).stream().map(hippoBean -> {
                    if (hippoBean instanceof HippoMirror) {
                        return ((HippoMirror) hippoBean).getReferencedBean();
                    }
                    return hippoBean;
                }
        ).collect(Collectors.toList());
    }

    /**
     * The method return an bean of the allowed types or null. Allowed types are InstagramImage and Image (Image Link)
     */
    public HippoBean getListicleItemImage() {
        return getOnlyChild(getImages());
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:extraLinks", allowModifications = false)
    public List<HippoCompound> getExtraLinks() {
        return getChildBeansByName("visitscotland:extraLinks",
                HippoCompound.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:diff")
    public String getDiff() {
        return getSingleProperty("visitscotland:diff");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationFlag")
    public Boolean getTranslationFlag() {
        return getSingleProperty("visitscotland:translationFlag");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }
}
