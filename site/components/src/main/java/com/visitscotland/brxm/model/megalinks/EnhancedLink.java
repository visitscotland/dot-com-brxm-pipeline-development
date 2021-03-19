package com.visitscotland.brxm.model.megalinks;

import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;

public class EnhancedLink extends FlatLink {

    private FlatImage image;
    private String teaser;
    private boolean featured;
    private String category;
    private String itineraryTransport;
    private int itineraryDays;

    public FlatImage getImage() {
        return image;
    }

    public void setImage(FlatImage image) {
        this.image = image;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItineraryTransport() {
        return itineraryTransport;
    }

    public void setItineraryTransport(String itineraryTransport) {
        this.itineraryTransport = itineraryTransport;
    }

    public int getItineraryDays() {
        return itineraryDays;
    }

    public void setItineraryDays(int itineraryDays) {
        this.itineraryDays = itineraryDays;
    }
}
