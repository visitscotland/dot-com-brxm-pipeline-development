package com.visitscotland.brxm.model;

import com.visitscotland.brxm.dms.DMSConstants.PSType;
import com.visitscotland.brxm.dms.model.LocationObject;

public class PSModule {

    private String title;
    private String description;
    private PSType category;

    private LocationObject location;


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
}
