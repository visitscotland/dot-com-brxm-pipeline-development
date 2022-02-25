package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.ListicleItem;
import com.visitscotland.dataobjects.DataType;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.List;

public class ListicleModule extends Module<ListicleItem> {

    private Integer index;
    private String title;
    private String subtitle;
    private HippoHtml description;
    private FlatImage image;
    private List<DataType> facilities;
    private List<FlatLink> links;

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

    public List<DataType> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<DataType> facilities) {
        this.facilities = facilities;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<FlatLink> getLinks() {
        return links;
    }

    public void setLinks(List<FlatLink> links) {
        this.links = links;
    }
}
