package com.visitscotland.brxm.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class MapsModule extends Module {
    private String id;
    private String title;
    private HippoHtml introduction;
    private String tabTitle;
    private JsonArray filters;
    private JsonObject geoJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HippoHtml getIntroduction() {
        return introduction;
    }

    public void setIntroduction(HippoHtml introduction) {
        this.introduction = introduction;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public JsonArray getFilters() {
        return filters;
    }

    public void setFilters(JsonArray filters) {
        this.filters = filters;
    }

    public JsonObject getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(JsonObject geoJson) {
        this.geoJson = geoJson;
    }


}
