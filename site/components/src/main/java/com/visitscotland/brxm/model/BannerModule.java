package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.Banner;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class BannerModule extends Module<Banner> {

    private HippoHtml copy;
    private FlatLink ctaLink;

    public HippoHtml getCopy() {
        return copy;
    }

    public void setCopy(HippoHtml copy) {
        this.copy = copy;
    }

    public FlatLink getCtaLink() {
        return ctaLink;
    }

    public void setCtaLink(FlatLink ctaLink) {
        this.ctaLink = ctaLink;
    }
}
