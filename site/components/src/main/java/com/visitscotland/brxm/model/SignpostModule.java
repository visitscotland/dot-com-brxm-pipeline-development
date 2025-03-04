package com.visitscotland.brxm.model;

import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class SignpostModule extends Module {

    private String title;
    private HippoHtml copy;
    private FlatLink cta;
    private FlatImage image;

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

    public FlatLink getCta() {
        return cta;
    }

    public void setCta(FlatLink cta) {
        this.cta = cta;
    }

    public FlatImage getImage() {
        return image;
    }

    public void setImage(FlatImage image) {
        this.image = image;
    }
}
