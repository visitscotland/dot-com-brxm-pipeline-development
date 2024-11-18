package com.visitscotland.brxm.model.megalinks;

import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;

import java.util.Date;
import java.util.List;

public class EnhancedLink extends FlatLink {

    private FlatImage image;
    private String teaser;
    private boolean featured;
    private String category;
    private String cta;
    private String itineraryTransport;
    private String youtubeId;
    private int itineraryDays;
    private Date publishedDate;
    private String source;
    private String contentType;
    private List<String> sector;
    private String skillLevel;
    private List<String> topic;
    private List<String> region;
    private String readTime;

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

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public List<String> getRegion() {
        return region;
    }

    public void setRegion(List<String> region) {
        this.region = region;
    }

    public List<String> getTopic() {
        return topic;
    }

    public void setTopic(List<String> topic) {
        this.topic = topic;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public List<String> getSector() {
        return sector;
    }

    public void setSector(List<String> sector) {
        this.sector = sector;
    }
}
