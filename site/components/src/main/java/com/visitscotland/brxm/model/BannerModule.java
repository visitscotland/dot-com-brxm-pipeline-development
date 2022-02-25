package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.Banner;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class BannerModule extends Module<Banner> {

    private String title;
    private HippoHtml copy;
    private FlatLink ctaLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
