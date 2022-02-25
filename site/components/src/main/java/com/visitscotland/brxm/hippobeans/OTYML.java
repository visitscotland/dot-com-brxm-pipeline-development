package com.visitscotland.brxm.hippobeans;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import java.util.List;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@HippoEssentialsGenerated(internalName = "visitscotland:OTYML")
@Node(jcrType = "visitscotland:OTYML")
public class OTYML extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:links", allowModifications = false)
    public List<HippoBean> getMegalinkItems() {
        return getLinkedBeans("visitscotland:links", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("visitscotland:introduction");
    }
}
