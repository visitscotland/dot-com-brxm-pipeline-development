package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.TourismInformation;

import java.util.List;

public class ICentreModule extends Module<TourismInformation> {

    private String title;
    private FlatImage image;
    private String description;
    private List<FlatLink> links;
    private FlatQuote quote;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FlatImage getImage() {
        return image;
    }

    public void setImage(FlatImage image) {
        this.image = image;
    }

    public List<FlatLink> getLinks() {
        return links;
    }

    public void setLinks(List<FlatLink> links) {
        this.links = links;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FlatQuote getQuote() {
        return quote;
    }

    public void setQuote(FlatQuote quote) {
        this.quote = quote;
    }

}
