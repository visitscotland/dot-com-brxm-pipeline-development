package com.visitscotland.brxm.model;


import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.Image;

import java.util.Locale;

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



    public FlatImage(){
    }

    /**
     * TODO: Remove this method after the refactoring of itineraries
     *
     * @deprecated Use ImageFactory.createImage(cmsModule, locale) instead
     */
    @Deprecated
    public FlatImage (Image cmsImage, Locale locale){
       this.cmsImage = cmsImage;
       this.credit = cmsImage.getCredit();
       if (locale != null){
            switch (locale.getLanguage()) {
                case "fr":
                    if (cmsImage.getFr() != null) {
                        this.altText = cmsImage.getFr().getAltText();
                        this.description = cmsImage.getFr().getCaption();
                    }
                    break;
                case "de":
                    if (cmsImage.getDe() != null) {
                        this.altText = cmsImage.getDe().getAltText();
                        this.description =cmsImage.getDe().getCaption();
                    }
                    break;
                case "es":
                    if (cmsImage.getEs() != null) {
                        this.altText = cmsImage.getEs().getAltText();
                        this.description =cmsImage.getEs().getCaption();
                    }
                    break;
                case "nl":
                    if (cmsImage.getNl() != null) {
                        this.altText = cmsImage.getNl().getAltText();
                        this.description =cmsImage.getNl().getCaption();
                    }
                    break;
                case "it":
                    if (cmsImage.getIt() != null) {
                        this.altText = cmsImage.getIt().getAltText();
                        this.description =cmsImage.getIt().getCaption();
                    }
                    break;
                default:
                    this.altText = cmsImage.getAltText();
                    this.description =cmsImage.getDescription();
            }
        }
    }

    /**
     * TODO: Remove this method after the refactoring of itineraries
     *
     * @deprecated Use ImageFactory.createImage(dmsProduct) instead
     */
    @Deprecated
    public FlatImage(JsonNode dmsImage, String productName) {
        final String MEDIA = "mediaUrl";
        final String CREDIT = "copyright";
        final String ALT_TEXT = "altText";

        this.externalImage = (dmsImage.has(MEDIA) ? dmsImage.get(MEDIA).asText() : null);
        this.credit = (dmsImage.has(CREDIT) ? dmsImage.get(CREDIT).asText() : null);
        this.description = (dmsImage.has(ALT_TEXT) ? dmsImage.get(ALT_TEXT).asText() : productName);
        this.altText = this.description;
    }

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
