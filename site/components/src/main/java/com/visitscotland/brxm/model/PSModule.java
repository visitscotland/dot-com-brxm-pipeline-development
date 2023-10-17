package com.visitscotland.brxm.model;

import com.visitscotland.brxm.dms.PSType;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.utils.Contract;

public class PSModule {

    private String title;
    private String description;
    private PSType category;
    private LocationObject location;
    private String domain;
    private String searchUrl;
    private String position;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PSType getCategory() {
        return category;
    }

    public void setCategory(PSType category) {
        this.category = category;
    }

    public LocationObject getLocation() {
        return location;
    }

    public void setLocation(LocationObject location) {
        this.location = location;
    }

    /** Non-JavaScript fall-back URL */
    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
