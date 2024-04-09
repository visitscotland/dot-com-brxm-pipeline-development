package com.visitscotland.brxm.hippobeans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@HippoEssentialsGenerated(internalName = "visitscotland:FormCompoundMarketo")
@Node(jcrType = "visitscotland:FormCompoundMarketo")
public class FormCompoundMarketo extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "visitscotland:jsonUrl")
    public String getJsonUrl() {
        return getSingleProperty("visitscotland:jsonUrl");
    }
}
