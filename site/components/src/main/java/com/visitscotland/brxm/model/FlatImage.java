package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.Image;

public class FlatImage extends IssueList {

    public enum Source {
        INSTAGRAM;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private Image cmsImage;
    private String externalImage;
    private String altText;
    private String credit;
    private String description;
    private Coordinates coordinates;
    private Source source;
    private String postUrl;
    private String location;

    public String getExternalImage() {
        return externalImage;
    }

    public void setExternalImage(String externalImage) {
        this.externalImage = externalImage;
    }
    public Image getCmsImage() {
        return cmsImage;
    }

    public void setCmsImage(Image cmsImage) {
        this.cmsImage = cmsImage;
    }
    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
