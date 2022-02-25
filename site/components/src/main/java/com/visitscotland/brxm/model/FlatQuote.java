package com.visitscotland.brxm.model;

import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class FlatQuote  {

    private String authorName;
    private String authorTitle;
    private HippoHtml quote;
    private FlatImage image;
    private EnhancedLink link;

    public FlatQuote() {
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorTitle() {
        return authorTitle;
    }

    public void setAuthorTitle(String authorTitle) {
        this.authorTitle = authorTitle;
    }

    public HippoHtml getQuote() {
        return quote;
    }

    public void setQuote(HippoHtml quote) {
        this.quote = quote;
    }

    public FlatImage getImage() {
        return image;
    }

    public void setImage(FlatImage image) {
        this.image = image;
    }

    public EnhancedLink getLink() {
        return link;
    }

    public void setLink(EnhancedLink link) {
        this.link = link;
    }
}