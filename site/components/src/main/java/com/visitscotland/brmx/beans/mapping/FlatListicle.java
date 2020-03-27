package com.visitscotland.brmx.beans.mapping;

import com.visitscotland.brmx.beans.ListicleItem;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.json.JSONObject;

import java.util.List;

public class FlatListicle {

    private String identifier;

    private Integer index;
    private String title;
    private String subTitle;
    private HippoHtml description;
    private FlatImage image;
    private List<JSONObject> facilities;
    private String location;
    private List<FlatLink> links;

    private String errorMessage;

    public FlatListicle(){

    }

    public FlatListicle(ListicleItem item){
        this.identifier = item.getIdentifier();
        this.title = item.getTitle();
        this.subTitle = item.getSubtitle();
        this.description = item.getDescription();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public List<JSONObject> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<JSONObject> facilities) {
        this.facilities = facilities;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }


    public List<FlatLink> getLinks() {
        return links;
    }

    public void setLinks(List<FlatLink> links) {
        this.links = links;
    }
}
