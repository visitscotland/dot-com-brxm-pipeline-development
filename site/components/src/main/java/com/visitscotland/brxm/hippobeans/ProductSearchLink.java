package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:ProductSearchLink")
@Node(jcrType = "visitscotland:ProductSearchLink")
public class ProductSearchLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:label")
    public String getLabel() {
        return getSingleProperty("visitscotland:label");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:search")
    public ProductsSearch getSearch() {
        return getBean("visitscotland:search", ProductsSearch.class);
    }
}
