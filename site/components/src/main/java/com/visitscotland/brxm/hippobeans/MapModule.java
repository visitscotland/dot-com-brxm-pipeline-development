package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import java.util.Calendar;
import java.util.List;

/** 
 * TODO: Beanwriter: Failed to create getter for node type: hippo:compound
 */
@HippoEssentialsGenerated(internalName = "visitscotland:MapModule")
@Node(jcrType = "visitscotland:MapModule")
public class MapModule extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:tabTitle")
    public String getTabTitle() {
        return getSingleProperty("visitscotland:tabTitle");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:copy")
    public HippoHtml getCopy() {
        return getHippoHtml("visitscotland:copy");
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getMultipleProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:diff")
    public String getDiff() {
        return getSingleProperty("visitscotland:diff");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationFlag")
    public Boolean getTranslationFlag() {
        return getSingleProperty("visitscotland:translationFlag");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationPriority")
    public String getTranslationPriority() {
        return getSingleProperty("visitscotland:translationPriority");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:categories", allowModifications = false)
    public List<MapCategory> getCategories() {
        return getChildBeansByName("visitscotland:categories",
                MapCategory.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:categories", allowModifications = false)
    public List<HippoCompound> getCompundCategories() {
        return getChildBeansByName("visitscotland:categories",
                HippoCompound.class);
    }

    public HippoCompound getFeaturedPlacesItem() {
        return getOnlyChild(getCompundCategories());
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:mapType")
    public String getMapType() {
        return getSingleProperty("visitscotland:mapType");
    }
}
