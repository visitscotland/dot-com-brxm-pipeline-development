package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoMirror;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.List;
import java.util.stream.Collectors;

@HippoEssentialsGenerated(internalName = "visitscotland:ArticleSection")
@Node(jcrType = "visitscotland:ArticleSection")
public class ArticleSection extends HippoCompound {

    public static final String COPY = "visitscotland:copy";
    public static final String MEDIA = "visitscotland:media";

    @HippoEssentialsGenerated(internalName = COPY)
    public HippoHtml getCopy() {
        return getHippoHtml(COPY);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:Quote")
    public Quote getQuote() {
        return getBean("visitscotland:Quote", Quote.class);
    }

    @HippoEssentialsGenerated(internalName = MEDIA, allowModifications = false)
    public List<HippoBean> getMedia() {
        return getChildBeansByName(MEDIA, HippoBean.class).stream().map(hippoBean -> {
                    if (hippoBean instanceof HippoMirror) {
                        return ((HippoMirror) hippoBean).getReferencedBean();
                    }
                    return hippoBean;
                }
        ).collect(Collectors.toList());
    }

    public HippoBean getMediaItem(){
        return getOnlyChild(getMedia());
    }

    protected <T> T getOnlyChild(List<T> children) {
        if (children.size() == 0) {
            return null;
        } else if (children.size() == 1) {
            return children.get(0);
        } else {
            return children.get(0);
        }
    }
}
