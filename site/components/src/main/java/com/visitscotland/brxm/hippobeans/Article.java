package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import java.util.List;
import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "visitscotland:Article")
@Node(jcrType = "visitscotland:Article")
public class Article extends BaseDocument {
    public static final String MEDIA = "visitscotland:media";
    public static final String COPY = "visitscotland:copy";
    public static final String PARAGRAPH = "visitscotland:paragraph";
    public static final String THEME = "visitscotland:theme";

    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:anchor")
    public String getAnchor() {
        return getSingleProperty("visitscotland:anchor");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:media", allowModifications = false)
    public List<HippoBean> getMedia() {
        return getMedia(MEDIA);
    }

    public HippoBean getMediaItem() {
        return getOnlyChild(getMedia());
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:copy")
    public HippoHtml getCopy() {
        return getHippoHtml(COPY);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:paragraph")
    public List<ArticleSection> getParagraph() {
        return getChildBeansByName(PARAGRAPH, ArticleSection.class);
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

    @HippoEssentialsGenerated(internalName = "visitscotland:translation")
    public String getTranslation() {
        return getSingleProperty("visitscotland:translation");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:translationDeadline")
    public Calendar getTranslationDeadline() {
        return getSingleProperty("visitscotland:translationDeadline");
    }
}
