package com.visitscotland.brxm.model;

import com.visitscotland.brxm.utils.HippoHtmlWrapper;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class SignpostModule extends Module {

    private String title;
    private HippoHtmlWrapper copy;
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

    public void setCopy(HippoHtmlWrapper copy) {
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
