package com.visitscotland.brxm.model.megalinks;

import com.visitscotland.brxm.model.FlatImage;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class SingleImageLinksModule extends LinksModule {

    private String innerTitle;
    private HippoHtml innerIntroduction;
    private FlatImage image;

    public String getInnerTitle() {
        return innerTitle;
    }

    public void setInnerTitle(String innerTitle) {
        this.innerTitle = innerTitle;
    }

    public HippoHtml getInnerIntroduction() {
        return innerIntroduction;
    }

    public void setInnerIntroduction(HippoHtml innerIntroduction) {
        this.innerIntroduction = innerIntroduction;
    }

    public FlatImage getImage() {
        return image;
    }

    public void setImage(FlatImage image) {
        this.image = image;
    }
}
