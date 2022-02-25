package com.visitscotland.brxm.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.Stop;
import com.visitscotland.dataobjects.DataType;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.math.BigDecimal;
import java.util.List;

public class ItineraryStopModule extends Module<Stop>{

    private Integer index;
    private String title;
    private String subTitle;
    private HippoHtml description;
    private FlatImage image;
    private String timeToExplore;
    private String tipsTitle;
    private FlatLink ctaLink;
    private HippoHtml tipsBody;
    private List<DataType> facilities;
    private JsonNode address;
    private String price;
    private JsonNode opening;
    private FlatLink openLink;
    private BigDecimal distance;

    private Coordinates coordinates;

    public String getIdentifier() {
        return getHippoBean() != null? getHippoBean().getIdentifier(): null;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HippoHtml getDescription() {
        return description;
    }

    public void setDescription(HippoHtml description) {
        this.description = description;
    }

    public FlatImage getImage() {
        return image;
    }

    public void setImage(FlatImage image) {
        this.image = image;
    }

    public String getTimeToExplore() {
        return timeToExplore;
    }

    public void setTimeToExplore(String timeToExplore) {
        this.timeToExplore = timeToExplore;
    }

    public String getTipsTitle() {
        return tipsTitle;
    }

    public void setTipsTitle(String tipsTitle) {
        this.tipsTitle = tipsTitle;
    }

    public HippoHtml getTipsBody() {
        return tipsBody;
    }

    public void setTipsBody(HippoHtml tipsBody) {
        this.tipsBody = tipsBody;
    }

    public List<DataType> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<DataType> facilities) {
        this.facilities = facilities;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public FlatLink getCtaLink() {
        return ctaLink;
    }

    public void setCtaLink(FlatLink ctaLink) {
        this.ctaLink = ctaLink;
    }

    public JsonNode getAddress() {
        return address;
    }

    public void setAddress(JsonNode address) {
        this.address = address;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subtitle) {
        this.subTitle = subtitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public JsonNode getOpening() {
        return opening;
    }

    public void setOpening(JsonNode opening) {
        this.opening = opening;
    }

    public FlatLink getOpenLink() {
        return openLink;
    }

    public void setOpenLink(FlatLink openLink) {
        this.openLink = openLink;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }
}
