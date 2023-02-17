package com.visitscotland.brxm.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class MapsModule extends Module {
    private String id;
    private String title;
    private HippoHtml introduction;
    private String tabTitle;
    private String detailsEndpoint;
    private String mapType;
    private ArrayNode filters;
    private ObjectNode geoJson;
    private ObjectNode mapPosition;

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

    public ArrayNode getFilters() {
        return filters;
    }

    public void setFilters(ArrayNode filters) {
        this.filters = filters;
    }

    public ObjectNode getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(ObjectNode geoJson) {
        this.geoJson = geoJson;
    }

    public ObjectNode getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(ObjectNode mapPosition) {
        this.mapPosition = mapPosition;
    }

    public String getDetailsEndpoint() {
        return detailsEndpoint;
    }

    public void setDetailsEndpoint(String detailsEndpoint) {
        this.detailsEndpoint = detailsEndpoint;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
